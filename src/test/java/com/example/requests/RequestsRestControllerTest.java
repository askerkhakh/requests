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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

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
        List<RequestDto> allRequests = restController.getAllRequests();

        assertEquals(new HashSet<>(requests), new HashSet<>(allRequests));
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