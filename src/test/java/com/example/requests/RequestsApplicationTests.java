package com.example.requests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RequestsApplicationTests {

    @Autowired
    private MockMvc mvc;

    private static final String TEST_REQUEST = "{\n" +
            "  \"person\": {\n" +
            "    \"surname\": \"Иванов\",\n" +
            "    \"name\": \"Иван\",\n" +
            "    \"patronymic\": \"Иванович\"\n" +
            "  }\n" +
            "}";

    @Test
    public void contextLoads() {
    }

    @Test
    @Before
    @DirtiesContext
    public void postRequestTest() throws Exception {
        mvc.perform(post("/requests")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TEST_REQUEST)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json(TEST_REQUEST));
    }

    @Test
    public void getRequestsTest() throws Exception {
        mvc.perform(get("/requests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json("[" + TEST_REQUEST + "]"));
    }

    @Test
    public void getRequestByIdTest() throws Exception {
        mvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json(TEST_REQUEST));
    }

}
