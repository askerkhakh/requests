package com.example.requests.service;

import com.example.requests.entity.Request;
import com.example.requests.repository.OrderByField;

import java.util.List;
import java.util.Map;

public interface RequestsService {

    Request createRequest(Request request);

    List<Request> getRequests(List<Map.Entry<String, String>> filterFields, List<OrderByField> orderByFields);

    Request getRequestById(long id);

    void setRequestProcessed(long id);
}
