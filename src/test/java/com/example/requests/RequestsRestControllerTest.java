package com.example.requests;

import com.example.requests.dto.PersonDto;
import com.example.requests.dto.RequestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RequestsRestControllerTest {

    @Autowired
    private RequestsRestController restController;

    public RequestDto postRequest(boolean full) {
        RequestDto requestDto = restController.postRequest(buildTestRequest());
        if (full) {
            requestDto.setDocuments(Collections.emptyList());
        }
        return requestDto;
    }

    private RequestDto buildTestRequest() {
        RequestDto requestDto = new RequestDto();
        PersonDto personDto = new PersonDto();
        personDto.setSurname("surname");
        personDto.setName("name");
        personDto.setPatronymic("patronymic");
        requestDto.setPerson(personDto);
        requestDto.setDate(LocalDate.now());
        requestDto.setServiceName("serviceName");
        return requestDto;
    }

    @Test
    @DirtiesContext
    public void postRequestTest() {
        RequestDto requestDto = postRequest(false);
        assertNotNull(requestDto.getId());
    }

    @Test
    public void getAllRequestsTest() {
        int count = new Random().nextInt(100);
        List<RequestDto> requests = new ArrayList<>();
        for (int i = 0; i < count - 1; i++) {
            requests.add(postRequest(false));
        }
        List<RequestDto> allRequests = restController.getRequests(Collections.emptyMap());

        assertEquals(new HashSet<>(requests), new HashSet<>(allRequests));
    }

    @Test
    @DirtiesContext
    public void getRequestsOrderedByNameTest() {
        RequestDto request1 = buildTestRequest();
        request1.getPerson().setName("a");
        RequestDto request2 = buildTestRequest();
        request2.getPerson().setName("b");
        RequestDto request3 = buildTestRequest();
        request3.getPerson().setName("c");
        List<RequestDto> requests = Arrays.asList(request1, request2, request3);
        List<RequestDto> postedRequests = new ArrayList<>();
        for (int i = requests.size(); i-- > 0;) {
            postedRequests.add(0, restController.postRequest(requests.get(i)));
        }
        Map<String, String> params = new HashMap<>();
        params.put(RequestsRestController.ORDER_BY_PARAM, "person.name");
        List<RequestDto> fetchedRequests = restController.getRequests(params);
        assertEquals(postedRequests, fetchedRequests);
    }

    @Test
    @DirtiesContext
    public void getRequestsOrderedByDateTest() {
        RequestDto request1 = buildTestRequest();
        request1.setDate(LocalDate.of(2000, 1, 1));
        RequestDto request2 = buildTestRequest();
        request2.setDate(LocalDate.of(2000, 1, 2));
        RequestDto request3 = buildTestRequest();
        request3.setDate(LocalDate.of(2000, 1, 3));
        List<RequestDto> requests = Arrays.asList(request1, request2, request3);
        List<RequestDto> postedRequests = new ArrayList<>();
        for (int i = requests.size(); i-- > 0;) {
            postedRequests.add(0, restController.postRequest(requests.get(i)));
        }
        Map<String, String> params = new HashMap<>();
        params.put(RequestsRestController.ORDER_BY_PARAM, "date");
        List<RequestDto> fetchedRequests = restController.getRequests(params);
        assertEquals(postedRequests, fetchedRequests);

        params.put(RequestsRestController.ORDER_BY_PARAM, "+date");
        List<RequestDto> fetchedRequests2 = restController.getRequests(params);
        assertEquals(postedRequests, fetchedRequests2);

        params.put(RequestsRestController.ORDER_BY_PARAM, "-date");
        List<RequestDto> fetchedRequests3 = restController.getRequests(params);
        Collections.reverse(postedRequests);
        assertEquals(postedRequests, fetchedRequests3);
    }

    @Test
    @DirtiesContext
    public void getRequestsFilteredByNameTest() {
        RequestDto request1 = buildTestRequest();
        request1.getPerson().setName("a");
        RequestDto request2 = buildTestRequest();
        request2.getPerson().setName("b");
        RequestDto request3 = buildTestRequest();
        request3.getPerson().setName("c");
        List<RequestDto> requests = Arrays.asList(request1, request2, request3);
        List<RequestDto> postedRequests = new ArrayList<>();
        for (RequestDto request : requests) {
            postedRequests.add(restController.postRequest(request));
        }
        Map<String, String> params = new HashMap<>();
        params.put("person.name", "b");
        List<RequestDto> fetchedRequests = restController.getRequests(params);
        assertEquals(Collections.singletonList(postedRequests.get(1)), fetchedRequests);
    }

    @Test
    @DirtiesContext
    public void getRequestByIdTest() {
        RequestDto requestDto = postRequest(true);
        assertNotNull(requestDto.getId());
        RequestDto foundRequestDto = restController.getRequestById(requestDto.getId());
        assertEquals(requestDto, foundRequestDto);
    }

    @Test
    @DirtiesContext
    public void getRequestsFilteredByDateTest() {
        RequestDto request1 = buildTestRequest();
        request1.setDate(LocalDate.of(2000, 1, 1));
        List<RequestDto> requests = Arrays.asList(request1, buildTestRequest(), buildTestRequest());
        List<RequestDto> postedRequests = new ArrayList<>();
        for (RequestDto request : requests) {
            postedRequests.add(restController.postRequest(request));
        }
        Map<String, String> params = new HashMap<>();
        params.put("date", "01/01/2000");
        List<RequestDto> fetchedRequests = restController.getRequests(params);
        assertEquals(Collections.singletonList(postedRequests.get(0)), fetchedRequests);
    }

    @Test
    @DirtiesContext
    public void getRequestsFilteredByStatusTest() {
        List<RequestDto> requests = Arrays.asList(buildTestRequest(), buildTestRequest(), buildTestRequest());
        List<RequestDto> postedRequests = new ArrayList<>();
        for (RequestDto request : requests) {
            postedRequests.add(restController.postRequest(request));
        }
        Map<String, String> params = new HashMap<>();
        params.put("status", "new");
        List<RequestDto> fetchedRequests = restController.getRequests(params);
        assertEquals(new HashSet<>(postedRequests), new HashSet<>(fetchedRequests));
    }

    @Test
    @DirtiesContext
    public void patchRequestTest() {
        RequestDto requestDto = postRequest(true);
        Long id = requireNonNull(requestDto.getId());
        restController.patchRequest(id, RequestsRestController.PROCESSED);
        requestDto.setStatus(RequestsRestController.PROCESSED);
        assertEquals(requestDto, restController.getRequestById(id));
    }
}