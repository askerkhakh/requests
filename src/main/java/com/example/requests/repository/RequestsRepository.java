package com.example.requests.repository;

import com.example.requests.entity.Request;

import java.util.List;
import java.util.Map;

public interface RequestsRepository {

    Request save(Request request);

    List<Request> getRequests(List<Map.Entry<String, String>> filterFields, List<String> orderByFields);

    Request getById(long id);
}
