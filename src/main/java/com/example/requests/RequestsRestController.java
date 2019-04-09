package com.example.requests;

import com.example.requests.dto.RequestDto;
import com.example.requests.entity.Request;
import com.example.requests.repository.OrderByField;
import com.example.requests.service.RequestsService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/requests")
public class RequestsRestController {
    // TODO: 08.04.19 Error handling and correct status codes

    public static final String ORDER_BY_PARAM = "order_by";
    public static final String PROCESSED = "processed";
    private final ModelMapper modelMapper;
    private final ModelMapper fullModelMapper;
    private final RequestsService requestsService;

    public RequestsRestController(ModelMapper modelMapper, ModelMapper fullModelMapper, RequestsService requestsService) {
        this.modelMapper = modelMapper;
        this.fullModelMapper = fullModelMapper;
        this.requestsService = requestsService;
    }

    @PostMapping
    public RequestDto postRequest(@RequestBody RequestDto requestDto) {
        return convertToDto(requestsService.createRequest(convertToFullEntity(requestDto)));
    }

    @GetMapping
    public List<RequestDto> getRequests(@RequestParam Map<String, String> params) {
        // TODO: 07.04.19 It's not very safe and flexible to use request parameters for filter and order settings, but I'll
        //  leave it as is for simplicity.
        String orderByParam = params.get(ORDER_BY_PARAM);
        List<OrderByField> orderByFields = orderByParamToOrderByFields(orderByParam);

        List<Map.Entry<String, String>> filterFields = params.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(ORDER_BY_PARAM))
                .collect(Collectors.toList());
        return requestsService.getRequests(filterFields, orderByFields)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private List<OrderByField> orderByParamToOrderByFields(String orderByParam) {
        List<OrderByField> orderByFields = new ArrayList<>();
        if (orderByParam != null) {
            for (String fieldString : orderByParam.split(",")) {
                if (!fieldString.isEmpty()) {
                    String field;
                    OrderByField.Direction direction;
                    switch (fieldString.charAt(0)) {
                        case '+':
                            field = fieldString.substring(1);
                            direction = OrderByField.Direction.ASCENDING;
                            break;
                        case '-':
                            field = fieldString.substring(1);
                            direction = OrderByField.Direction.DESCENDING;
                            break;
                        default:
                            field = fieldString;
                            direction = OrderByField.Direction.ASCENDING;
                            break;
                    }
                    orderByFields.add(new OrderByField(field, direction));
                }
            }
        }
        return orderByFields;
    }

    @GetMapping(path = "/{id}")
    public RequestDto getRequestById(@PathVariable long id) {
        return convertToFullDto(requestsService.getRequestById(id));
    }

    @PatchMapping(path = "/{id}")
    public void patchRequest(@PathVariable long id, @RequestParam(value = "status") String status) {
        if (PROCESSED.equals(status)) {
            requestsService.setRequestProcessed(id);
        }
    }

    private RequestDto convertToDto(Request request) {
        return modelMapper.map(request, RequestDto.class);
    }

    private RequestDto convertToFullDto(Request request) {
        return fullModelMapper.map(request, RequestDto.class);
    }

    private Request convertToFullEntity(RequestDto requestDto) {
        return fullModelMapper.map(requestDto, Request.class);
    }

}
