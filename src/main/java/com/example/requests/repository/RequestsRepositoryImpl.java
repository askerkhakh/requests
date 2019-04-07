package com.example.requests.repository;

import com.example.requests.entity.Request;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class RequestsRepositoryImpl implements RequestsRepository {

    private final EntityManager entityManager;

    public RequestsRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Request save(Request request) {
        entityManager.persist(request);
        return request;
    }

    @Override
    public List<Request> getAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> query = cb.createQuery(Request.class);
        query.select(query.from(Request.class));
        return entityManager.createQuery(query).getResultList();
    }

}
