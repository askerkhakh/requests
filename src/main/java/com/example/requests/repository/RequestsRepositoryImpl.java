package com.example.requests.repository;

import com.example.requests.entity.Request;
import com.example.requests.entity.RequestStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            ParameterValuePair parameterValuePair = parameterValuePairFromStrings(cb, filterField.getKey(), filterField.getValue());
            parameterValuePairs.add(parameterValuePair);
            filterPredicates.add(cb.equal(getPath(from, filterField.getKey()), parameterValuePair.parameter));
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
            uncheckedSetParameter(typedQuery, parameterValuePair);
        }
        return typedQuery.getResultList();
    }

    @SuppressWarnings(value = "unchecked")
    private void uncheckedSetParameter(TypedQuery<Request> typedQuery, ParameterValuePair parameterValuePair) {
        typedQuery.setParameter(parameterValuePair.parameter, parameterValuePair.value);
    }

    private static ParameterValuePair parameterValuePairFromStrings(CriteriaBuilder criteriaBuilder, String fieldName, String value) {
        // TODO: 08.04.19 We can use here reflection for determining the type of field (and so the type of parameter),
        //  but for simplicity lets keep it hardcoded
        switch (fieldName) {
            case "person.surname":
            case "person.name":
            case "person.patronymic":
            case "serviceName":
                return new ParameterValuePair<>(criteriaBuilder.parameter(String.class), value);
            case "date":
                return new ParameterValuePair<>(criteriaBuilder.parameter(LocalDate.class), LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            case "status":
                return new ParameterValuePair<>(criteriaBuilder.parameter(RequestStatus.class), RequestStatus.valueOf(value.toUpperCase()));
            default:
                throw new AssertionError(String.format("Parameter conversion for field %s does not supported", fieldName));
        }
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

    private static class ParameterValuePair<T> {
        private final ParameterExpression<T> parameter;
        private final T value;

        ParameterValuePair(ParameterExpression<T> parameter, T value) {
            this.parameter = parameter;
            this.value = value;
        }
    }
}
