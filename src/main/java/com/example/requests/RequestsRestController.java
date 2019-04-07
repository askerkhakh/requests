package com.example.requests;

import com.example.requests.dto.RequestDto;
import com.example.requests.entity.Request;
import com.example.requests.service.RequestsService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/requests/")
public class RequestsRestController {

    private static final String ORDER_BY_PARAM = "orderBy";
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
    public List<RequestDto> getAllRequests(@RequestParam Map<String, String> params) {
        // TODO: 07.04.19 It's not very safe and flexible to use request parameters for filter and order settings, but I'll
        //  leave it as is for simplicity.
        String orderByParam = params.get(ORDER_BY_PARAM);
        List<String> orderByFields = Collections.emptyList();
        if (orderByParam != null) {
            orderByFields = Arrays.asList(orderByParam.split(","));
        }
        List<Map.Entry<String, String>> filterFields = params.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(ORDER_BY_PARAM))
                .collect(Collectors.toList());
        return requestsService.getAllRequests(filterFields, orderByFields)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "{id}")
    public RequestDto getById(@PathVariable long id) {
        return convertToDto(requestsService.getById(id));
    }

    private RequestDto convertToDto(Request request) {
        return modelMapper.map(request, RequestDto.class);
    }

    private Request convertToEntity(RequestDto requestDto) {
        return modelMapper.map(requestDto, Request.class);
    }

}
