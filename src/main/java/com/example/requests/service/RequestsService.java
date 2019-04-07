package com.example.requests.service;

import com.example.requests.entity.Request;

import java.util.List;

public interface RequestsService {

    Request createRequest(Request request);

    List<Request> getAllRequests();

}
