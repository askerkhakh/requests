package com.example.requests;

import com.example.requests.dto.RequestDto;
import com.example.requests.entity.Request;
import com.example.requests.service.RequestsService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/requests/")
public class RequestsRestController {

    private final ModelMapper modelMapper;
    private final RequestsService requestsService;

    public RequestsRestController(ModelMapper modelMapper, RequestsService requestsService) {
        this.modelMapper = modelMapper;
        this.requestsService = requestsService;
    }

    @PostMapping(path = "post")
    public RequestDto postRequest(@RequestBody RequestDto requestDto) {
        return convertToDto(requestsService.createRequest(convertToEntity(requestDto)));
    }

    private RequestDto convertToDto(Request request) {
        return modelMapper.map(request, RequestDto.class);
    }

    private Request convertToEntity(RequestDto requestDto) {
        return modelMapper.map(requestDto, Request.class);
    }


}
