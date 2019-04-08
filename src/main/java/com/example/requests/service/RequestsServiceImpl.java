package com.example.requests.service;

import com.example.requests.entity.Request;
import com.example.requests.entity.RequestStatus;
import com.example.requests.repository.RequestsRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RequestsServiceImpl implements RequestsService {

    private final RequestsRepository requestsRepository;

    public RequestsServiceImpl(RequestsRepository requestsRepository) {
        this.requestsRepository = requestsRepository;
    }

    @Override
    public Request createRequest(Request request) {
        request.setStatus(RequestStatus.NEW);
        return requestsRepository.save(request);
    }

    @Override
    public List<Request> getRequests(List<Map.Entry<String, String>> filterFields, List<String> orderByFields) {
        return requestsRepository.getRequests(filterFields, orderByFields);
    }

    @Override
    public Request getRequestById(long id) {
        return requestsRepository.getById(id, true);
    }

}
