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

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RequestsRestControllerTest {

    @Autowired
    private RequestsRestController restController;

    public RequestDto postRequest() {
        return restController.postRequest(buildTestRequest());
    }

    private RequestDto buildTestRequest() {
        RequestDto requestDto = new RequestDto();
        PersonDto personDto = new PersonDto();
        personDto.setSurname("surname");
        personDto.setName("name");
        personDto.setPatronymic("patronymic");
        requestDto.setPerson(personDto);
        return requestDto;
    }

    @Test
    @DirtiesContext
    public void postRequestTest() {
        RequestDto requestDto = postRequest();
        assertNotNull(requestDto.getId());
    }

    @Test
    public void getAllRequestsTest() {
        int count = new Random().nextInt(100);
        List<RequestDto> requests = new ArrayList<>();
        for (int i = 0; i < count - 1; i++) {
            requests.add(postRequest());
        }
        List<RequestDto> allRequests = restController.getAllRequests(Collections.emptyMap());

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
        params.put("orderBy", "person.name");
        List<RequestDto> fetchedRequests = restController.getAllRequests(params);
        assertEquals(postedRequests, fetchedRequests);
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
        List<RequestDto> fetchedRequests = restController.getAllRequests(params);
        assertEquals(Collections.singletonList(postedRequests.get(1)), fetchedRequests);
    }

    @Test
    @DirtiesContext
    public void getByIdTest() {
        RequestDto requestDto = postRequest();
        assertNotNull(requestDto.getId());
        RequestDto foundRequestDto = restController.getById(requestDto.getId());
        assertEquals(requestDto, foundRequestDto);
    }
}