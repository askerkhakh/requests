package com.example.requests.config;

import com.example.requests.dto.RequestDto;
import com.example.requests.entity.Request;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestConfig {

    @Bean
    public ModelMapper fullModelMapper() {
        // TODO: 08.04.19 map DocumentDto.data base64 string to Document.data byte array
        return defaultModleMapper();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = defaultModleMapper();
        modelMapper.getTypeMap(Request.class, RequestDto.class).addMappings(mapper -> mapper.skip(RequestDto::setDocuments));
        return modelMapper;
    }

    private ModelMapper defaultModleMapper() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<Request, RequestDto> typeMap = modelMapper.createTypeMap(Request.class, RequestDto.class);
        // status property is mapped only one way: from entity to dto
        typeMap.addMappings(mapping -> mapping.using(ctx -> ctx.getSource().toString().toLowerCase()).map(Request::getStatus, RequestDto::setStatus));
        return modelMapper;
    }

}
