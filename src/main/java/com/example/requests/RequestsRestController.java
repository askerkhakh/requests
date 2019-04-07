package com.example.requests;

import com.example.requests.dto.RequestDto;
import com.example.requests.entity.Request;
import com.example.requests.service.RequestsService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping(path = "all")
    public List<RequestDto> getAllRequests() {
        return requestsService.getAllRequests()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private RequestDto convertToDto(Request request) {
        return modelMapper.map(request, RequestDto.class);
    }

    private Request convertToEntity(RequestDto requestDto) {
        return modelMapper.map(requestDto, Request.class);
    }

}
