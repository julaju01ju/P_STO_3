package com.javamentor.qa.platform.webapp.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.dto.UserDtoTest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ali Veliev 02.12.2021
 */

public class TestUserResourceController extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answers.yml",
            "dataset/UserResourceController/questions.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getUserById() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/101")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.email").value("SomeEmail@mail.com"))
                .andExpect(jsonPath("$.fullName").value("Constantin"))
                .andExpect(jsonPath("$.linkImage").value("link"))
                .andExpect(jsonPath("$.city").value("Moscow"))
                .andExpect(jsonPath("$.reputation").value(101));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/users.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/answers.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/questions.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/reputations.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/tag.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/question_has_tag.yml",
            "dataset/UserResourceController/GetUserByIdWithTop3Tags/roles.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getUserByIdWithTop3Tags() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user@mail.ru"))
                .andExpect(jsonPath("$.fullName").value("USER"))
                .andExpect(jsonPath("$.linkImage").value("image"))
                .andExpect(jsonPath("$.city").value("city"))
                .andExpect(jsonPath("$.reputation").value(42))
                .andExpect(jsonPath("$.topTags.length()").value(3))
                .andExpect(jsonPath("$.topTags[0].name").value("spring"))
                .andExpect(jsonPath("$.topTags[1].name").value("hibernate"))
                .andExpect(jsonPath("$.topTags[2].name").value("postgres"));
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answers.yml",
            "dataset/UserResourceController/questions.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"}, disableConstraints = true, cleanBefore = true)
    public void shouldNotGetUserById() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/120")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithOutPageParam() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithOutItemsParam() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=1")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(3))
                .andExpect(jsonPath("$.totalResultCount").value(23))
                .andExpect(jsonPath("$.itemsOnPage").value(10));
    }


    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithPage2Items1() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=2&items=1")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(2))
                .andExpect(jsonPath("$.totalPageCount").value(23))
                .andExpect(jsonPath("$.totalResultCount").value(23))
                .andExpect(jsonPath("$.itemsOnPage").value(1))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");
        Assertions.assertTrue(list.size() == 1);
        Assertions.assertTrue((int) list.get(0).get("id") == 121);
    }


    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithPage1Items3AndFilter() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=1&items=3&filter=@mail")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(8))
                .andExpect(jsonPath("$.totalResultCount").value(23))
                .andExpect(jsonPath("$.itemsOnPage").value(3))
                .andExpect(jsonPath("$.items[0].id").value(122))
                .andExpect(jsonPath("$.items[0].email").value("SomeEmail@mail.mail"))
                .andExpect(jsonPath("$.items[0].fullName").value("Constantin"))
                .andExpect(jsonPath("$.items[1].id").value(121))
                .andExpect(jsonPath("$.items[1].email").value("SomeEmail@mail.mail"))
                .andExpect(jsonPath("$.items[1].fullName").value("Constantin"))
                .andExpect(jsonPath("$.items[2].id").value(120))
                .andExpect(jsonPath("$.items[2].email").value("SomeEmail@mail.mail"))
                .andExpect(jsonPath("$.items[2].fullName").value("Constantin"));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithPage1Items3AndEmptyFilter() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=1&items=3&filter=")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(8))
                .andExpect(jsonPath("$.totalResultCount").value(23))
                .andExpect(jsonPath("$.itemsOnPage").value(3))
                .andExpect(jsonPath("$.items[0].id").value(122))
                .andExpect(jsonPath("$.items[0].email").value("SomeEmail@mail.mail"))
                .andExpect(jsonPath("$.items[0].fullName").value("Constantin"));
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithPage1Items50() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=1&items=50")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(1))
                .andExpect(jsonPath("$.totalResultCount").value(23))
                .andExpect(jsonPath("$.itemsOnPage").value(50))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");
        Assertions.assertTrue(list.size() == 23);
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/roles.yml",
            "dataset/UserResourceController/GetAllUsersOrderByPersistDatePagination/users.yml",
            "dataset/UserResourceController/reputations.yml"}, disableConstraints = true, cleanBefore = true)
    public void getAllUsersOrderByPersistDatePaginationWithPage1Items5CheckSorting() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/new?page=1&items=5")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(5))
                .andExpect(jsonPath("$.totalResultCount").value(23))
                .andExpect(jsonPath("$.itemsOnPage").value(5))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");
        Assertions.assertTrue((int) list.get(0).get("id") == 122);
        Assertions.assertTrue((int) list.get(1).get("id") == 121);
        Assertions.assertTrue((int) list.get(2).get("id") == 120);
        Assertions.assertTrue((int) list.get(3).get("id") == 119);
        Assertions.assertTrue((int) list.get(4).get("id") == 118);
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answers.yml",
            "dataset/UserResourceController/questions.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getPageAllUserSortedByReputation() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/reputation?page=1&items=5")
                        .header(AUTHORIZATION, USER_TOKEN).header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(2))
                .andExpect(jsonPath("$.totalResultCount").value(9))
                .andExpect(jsonPath("$.itemsOnPage").value(5))
                .andExpect(jsonPath("$.items[0].id").value(101))
                .andExpect(jsonPath("$.items[0].email").value("SomeEmail@mail.com"))
                .andExpect(jsonPath("$.items[0].fullName").value("Constantin"))
                .andExpect(jsonPath("$.items[0].linkImage").value("link"))
                .andExpect(jsonPath("$.items[0].city").value("Moscow"))
                .andExpect(jsonPath("$.items[0].reputation").value(202))
                .andExpect(jsonPath("$.items.size()").value(5));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answers.yml",
            "dataset/UserResourceController/questions.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getPageAllUserSortedByReputationWithFilter() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/reputation?page=1&items=5&filter=mail")
                        .header(AUTHORIZATION, USER_TOKEN).header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(2))
                .andExpect(jsonPath("$.totalResultCount").value(9))
                .andExpect(jsonPath("$.itemsOnPage").value(5))
                .andExpect(jsonPath("$.items[0].id").value(101))
                .andExpect(jsonPath("$.items[0].email").value("SomeEmail@mail.com"))
                .andExpect(jsonPath("$.items[0].fullName").value("Constantin"))
                .andExpect(jsonPath("$.items[0].linkImage").value("link"))
                .andExpect(jsonPath("$.items[0].city").value("Moscow"))
                .andExpect(jsonPath("$.items[0].reputation").value(202))
                .andExpect(jsonPath("$.items.size()").value(5));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/users.yml",
            "dataset/UserResourceController/answers.yml",
            "dataset/UserResourceController/questions.yml",
            "dataset/UserResourceController/reputations.yml",
            "dataset/UserResourceController/roles.yml"
    },
            disableConstraints = true, cleanBefore = true)
    public void getPageAllUserSortedByReputationWithEmptyFilter() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/reputation?page=1&items=5&filter=")
                        .header(AUTHORIZATION, USER_TOKEN).header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(2))
                .andExpect(jsonPath("$.totalResultCount").value(9))
                .andExpect(jsonPath("$.itemsOnPage").value(5))
                .andExpect(jsonPath("$.items[0].id").value(101))
                .andExpect(jsonPath("$.items[0].email").value("SomeEmail@mail.com"))
                .andExpect(jsonPath("$.items[0].fullName").value("Constantin"))
                .andExpect(jsonPath("$.items[0].linkImage").value("link"))
                .andExpect(jsonPath("$.items[0].city").value("Moscow"))
                .andExpect(jsonPath("$.items[0].reputation").value(202))
                .andExpect(jsonPath("$.items.size()").value(5));
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetAllUsersSortedByVote/roles.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/users.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/reputations.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/questions.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/answers.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/votes_on_questions.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/votes_on_answers.yml"
            }, disableConstraints = true, cleanBefore = true)
    public void getPageAllUsersSortedByVoteCheckSortingDESCWithPage1Items5() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/vote?page=1&items=5")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.totalPageCount").value(4))
                .andExpect(jsonPath("$.totalResultCount").value(20))
                .andExpect(jsonPath("$.itemsOnPage").value(5))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");
        System.out.println(list.size());
        Assertions.assertTrue((int) list.get(0).get("id") == 105);
        Assertions.assertTrue((int) list.get(1).get("id") == 104);
        Assertions.assertTrue((int) list.get(2).get("id") == 103);
        Assertions.assertTrue((int) list.get(3).get("id") == 102);
        Assertions.assertTrue((int) list.get(4).get("id") == 101);
    }

    @Test
    @DataSet(value = {
            "dataset/UserResourceController/GetAllUsersSortedByVote/roles.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/users.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/reputations.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/questions.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/answers.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/votes_on_questions.yml",
            "dataset/UserResourceController/GetAllUsersSortedByVote/votes_on_answers.yml"
    }, disableConstraints = true, cleanBefore = true)
    public void GetPageAllUsersSortedByVoteCheckSortingASCWithPage4Items5() throws Exception {

        String USER_TOKEN = getToken("user@mail.ru", "USER");

        String pageUsers = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/vote?page=4&items=5")
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber").value(4))
                .andExpect(jsonPath("$.totalPageCount").value(4))
                .andExpect(jsonPath("$.totalResultCount").value(20))
                .andExpect(jsonPath("$.itemsOnPage").value(5))
                .andReturn().getResponse().getContentAsString();

        List<HashMap> list = JsonPath.read(pageUsers, "$.items");
        System.out.println(list.size());
        Assertions.assertTrue((int) list.get(4).get("id") == 106);
        Assertions.assertTrue((int) list.get(3).get("id") == 107);
        Assertions.assertTrue((int) list.get(2).get("id") == 108);
        Assertions.assertTrue((int) list.get(1).get("id") == 109);
        Assertions.assertTrue((int) list.get(0).get("id") == 110);
    }

    @Test
    @DataSet(value = {"dataset/UserResourceController/updatePassword/users.yml"},
            disableConstraints = true, cleanBefore = true)
    public void updatePassword() throws Exception {

        String USER_TOKEN = getToken("user130@mail.ru", "USER");

        UserDtoTest userDtoTest = new UserDtoTest();
        userDtoTest.setId(130L);
        userDtoTest.setPassword("USER");

        // the same password
        mockMvc.perform(MockMvcRequestBuilders.put("/api/{userId}/change/password", 130L)
                        .content(new ObjectMapper().writeValueAsString(userDtoTest))
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // password is not correct(too short)
        userDtoTest.setPassword("Ty55");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/{userId}/change/password", 130L)
                        .content(new ObjectMapper().writeValueAsString(userDtoTest))
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // password is not correct(wrong symbols)
        userDtoTest.setPassword("111111111111111111");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/{userId}/change/password", 130L)
                        .content(new ObjectMapper().writeValueAsString(userDtoTest))
                        .header(AUTHORIZATION, USER_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // password is correct
        userDtoTest.setPassword("TtF@R1");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/{userId}/change/password", 130L)
                        .content(new ObjectMapper().writeValueAsString(userDtoTest))
                        .header(AUTHORIZATION, USER_TOKEN))
                  .andDo(print())
                  .andExpect(status().isOk());
    }
}
