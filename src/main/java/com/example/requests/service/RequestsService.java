package com.example.requests.service;

import com.example.requests.entity.Request;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class RequestsService {

    private final EntityManager entityManager;

    public RequestsService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Request createRequest(Request request) {
        entityManager.persist(request);
        return request;
    }
}
