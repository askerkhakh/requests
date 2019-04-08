package com.example.requests.config;

import com.example.requests.dto.RequestDto;
import com.example.requests.entity.Request;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestConfig {

    @Bean
    public ModelMapper fullModelMapper() {
        // TODO: 08.04.19 map DocumentDto.data base64 string to Document.data byte array
        return new ModelMapper();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Request.class, RequestDto.class).addMappings(mapper -> mapper.skip(RequestDto::setDocuments));
        return modelMapper;
    }

}
