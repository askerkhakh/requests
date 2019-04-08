package com.example.requests.repository;

import com.example.requests.entity.Request;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<Request> getRequests(List<Map.Entry<String, String>> filterFields, List<String> orderByFields) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> query = cb.createQuery(Request.class);
        Root<Request> from = query.from(Request.class);
        query.select(from);

        List<Predicate> filterPredicates = new ArrayList<>();
        List<ParameterValuePair> parameterValuePairs = new ArrayList<>();
        for (Map.Entry<String, String> filterField : filterFields) {
            ParameterExpression<String> parameter = cb.parameter(String.class);
            parameterValuePairs.add(new ParameterValuePair(parameter, filterField.getValue()));
            filterPredicates.add(cb.equal(getPath(from, filterField.getKey()), parameter));
        }
        query.where(filterPredicates.toArray(new Predicate[0]));


        List<Order> orderList = new ArrayList<>();
        for (String orderByField : orderByFields) {
            // TODO: 07.04.19 support descending order
            orderList.add(cb.asc(getPath(from, orderByField)));
        }
        query.orderBy(orderList);

        TypedQuery<Request> typedQuery = entityManager.createQuery(query);
        for (ParameterValuePair parameterValuePair : parameterValuePairs) {
            typedQuery.setParameter(parameterValuePair.parameter, parameterValuePair.value);
        }
        return typedQuery.getResultList();
    }

    private Path getPath(Root<Request> from, String field) {
        Path path = from;
        for (String item : field.split("\\.")) {
            path = path.get(item);
        }
        return path;
    }

    @Override
    public Request getById(long id, boolean fetchFull) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> query = cb.createQuery(Request.class);
        Root<Request> from = query.from(Request.class);
        ParameterExpression<Long> parameter = cb.parameter(Long.class);
        query.select(from).where(cb.equal(from.get("id"), parameter));
        Request request = entityManager.createQuery(query).setParameter(parameter, id).getSingleResult();
        if (fetchFull) {
            request.getDocuments().size();
        }
        return request;
    }

    private class ParameterValuePair {
        private final ParameterExpression<String> parameter;
        private final String value;

        ParameterValuePair(ParameterExpression<String> parameter, String value) {
            this.parameter = parameter;
            this.value = value;
        }
    }
}
