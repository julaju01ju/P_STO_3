package com.javamentor.qa.platform.webapp.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.models.dto.AuthenticationRequest;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ali Veliev 15.12.2021
 */

@DBRider
@SpringBootTest(classes = JmApplication.class)
@TestPropertySource(properties = "spring.config.location = src/test/resources/application-test.properties")
@AutoConfigureMockMvc
@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
public class TestQuestionResourceContoller {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void getQuestionById() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/101")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.authorId").value(101))
                .andExpect(jsonPath("$.authorReputation").value(101))
                .andExpect(jsonPath("$.authorName").value("Constantin"))
                .andExpect(jsonPath("$.authorImage").value("link"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.viewCount").value(0))
                .andExpect(jsonPath("$.countAnswer").value(1))
                .andExpect(jsonPath("$.countValuable").value(1))
                .andExpect(jsonPath("$.persistDateTime").value("2021-12-06T03:00:00"))
                .andExpect(jsonPath("$.lastUpdateDateTime").value("2021-12-06T03:00:00"));
    }


    @Test
    @DataSet(value = {"dataset/QuestionResourceController/users.yml",
            "dataset/QuestionResourceController/tag.yml",
            "dataset/QuestionResourceController/votes_on_questions.yml",
            "dataset/QuestionResourceController/answers.yml",
            "dataset/QuestionResourceController/questions.yml",
            "dataset/QuestionResourceController/question_has_tag.yml",
            "dataset/QuestionResourceController/reputations.yml",
            "dataset/QuestionResourceController/roles.yml"})
    void shouldNotGetQuestionById() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("someHardPassword");
        authenticationRequest.setUsername("SomeEmail@mail.mail");

        String USER_TOKEN = mockMvc.perform(
                        post("/api/auth/token/")
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        USER_TOKEN = "Bearer " + USER_TOKEN.substring(USER_TOKEN.indexOf(":") + 2, USER_TOKEN.length() - 2);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/question/105")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }
}
