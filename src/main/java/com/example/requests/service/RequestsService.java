package com.example.requests.service;

import com.example.requests.entity.Request;

import java.util.List;
import java.util.Map;

public interface RequestsService {

    Request createRequest(Request request);

    List<Request> getAllRequests(List<Map.Entry<String, String>> filterFields, List<String> orderByFields);

    Request getById(long id);
}
