package com.example.requests.service;

import com.example.requests.entity.Request;
import com.example.requests.repository.RequestsRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RequestsServiceImpl implements RequestsService {

    private final RequestsRepository requestsRepository;

    public RequestsServiceImpl(RequestsRepository requestsRepository) {
        this.requestsRepository = requestsRepository;
    }

    @Override
    public Request createRequest(Request request) {
        return requestsRepository.save(request);
    }

    @Override
    public List<Request> getAllRequests() {
        return requestsRepository.getAll();
    }

}
