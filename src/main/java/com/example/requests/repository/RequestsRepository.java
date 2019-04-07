package com.example.requests.repository;

import com.example.requests.entity.Request;

import java.util.List;

public interface RequestsRepository {

    Request save(Request request);

    List<Request> getAll();

}
