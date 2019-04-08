package com.example.requests.config;

import com.example.requests.dto.DocumentDto;
import com.example.requests.dto.RequestDto;
import com.example.requests.entity.Document;
import com.example.requests.entity.Request;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class RestConfig {

    @Bean
    public ModelMapper fullModelMapper() {
        ModelMapper modelMapper = defaultModleMapper();


        TypeMap<Document, DocumentDto> documentToDocumentDtoTypeMap = modelMapper.createTypeMap(Document.class, DocumentDto.class);
        documentToDocumentDtoTypeMap.addMappings(
                mapping -> mapping
                        .using(ctx -> Base64.getEncoder().encodeToString((byte[]) ctx.getSource()))
                        .map(Document::getData, DocumentDto::setData)
        );
        TypeMap<DocumentDto, Document> documentDtoToDocumentTypeMap = modelMapper.createTypeMap(DocumentDto.class, Document.class);
        documentDtoToDocumentTypeMap.addMappings(
                mapping -> mapping
                        .using(ctx -> Base64.getDecoder().decode((String) ctx.getSource()))
                        .map(DocumentDto::getData, Document::setData)
        );

        modelMapper.getTypeMap(Request.class, RequestDto.class).addMappings(
                mapping -> mapping
                        .using(ctx -> ((Set<Document>) ctx.getSource())
                                .stream()
                                .map(document -> modelMapper.map(document, DocumentDto.class))
                                .collect(Collectors.toList())
                        )
                        .map(Request::getDocuments, RequestDto::setDocuments)
        );
        modelMapper.createTypeMap(RequestDto.class, Request.class).addMappings(
                mapping -> mapping
                        .using(ctx -> ((List<DocumentDto>) ctx.getSource())
                                .stream()
                                .map(documentDto -> modelMapper.map(documentDto, Document.class))
                                .collect(Collectors.toSet())
                        )
                        .map(RequestDto::getDocuments, Request::setDocuments)
        );

        return modelMapper;
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
