package quest.quest03.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import quest.quest01.domain.type.ResponseMessage;
import quest.quest03.dto.request.DepartmentInfo;
import quest.quest03.dto.request.Relation;
import quest.quest03.dto.response.DepartmentResponse;
import quest.quest03.service.DepartmentService;

import javax.persistence.EntityManager;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DepartmentControllerTest {

    @Autowired
    EntityManager em;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DepartmentService service;

    @Test
    @DisplayName("getTotalCount")
    public void getTotalCount() throws Exception {

        DepartmentInfo dmInfo1 = new DepartmentInfo("QA", 100);
        DepartmentInfo dmInfo2 = new DepartmentInfo("DEV", 100);
        DepartmentInfo dmInfo3 = new DepartmentInfo("C", 100);
        DepartmentInfo dmInfo4 = new DepartmentInfo("D", 100);
        service.departmentInfoRegister(dmInfo1);
        service.departmentInfoRegister(dmInfo2);
        service.departmentInfoRegister(dmInfo3);
        service.departmentInfoRegister(dmInfo4);

        Relation relation1 = new Relation("QA", "DEV");
        Relation relation2 = new Relation("DEV", "C");
        Relation relation3 = new Relation("C", "D");
        service.relationRegister(relation1);
        service.relationRegister(relation2);
        service.relationRegister(relation3);


        mockMvc.perform(get("/v3/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"departmentName\" : \"QA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("QA"))
                .andExpect(jsonPath("$.data").value(400))
                .andDo(print());
    }

    @Test
    @DisplayName("departmentInfoRegisterTest")
    public void infoRegisterTest() throws Exception {
        mockMvc.perform(post("/v3/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"department\" : \"QA\", \"count\" : \"100\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("부서 등록 완료"))
                .andExpect(jsonPath("$.data.name").value("QA"))
                .andDo(print());
    }

    @Test
    @DisplayName("relationRegisterTest")
    public void relationRegister() throws Exception {
        mockMvc.perform(post("/v3/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"department\" : \"QA\", \"count\" : \"100\"}"))
                .andDo(print());

        mockMvc.perform(post("/v3/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"department\" : \"DEV\", \"count\" : \"200\"}"))
                .andDo(print());

        em.flush();
        em.clear();


        mockMvc.perform(post("/v3/relation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                                .param("parent", "QA")
                                .param("child","DEV"))
//                        .content("{\"parent\" : \"QA\",\"child\": \"DEV\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("부서 관계 등록이 완료 되었습니다."))
                .andExpect(jsonPath("$.data.name").value("QA"))
                .andDo(print());

    }

}