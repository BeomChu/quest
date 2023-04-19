package quest.quest03.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import quest.quest01.type.Response;
import quest.quest03.domain.Department;
import quest.quest03.service.DepartmentService;

import javax.persistence.EntityManager;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static quest.quest01.type.Response.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DepartmentControllerTest {

    @Autowired
    EntityManager em;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("부서등록테스트")
    public void infoTest01() throws Exception {
        mockMvc.perform(get("/v3/department")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("request", "QA, 100"))
                .andExpect(content().string("QA, 100"))
                .andDo(print());
    }


    @Test
    @DisplayName("소문자로는 부서등록이 안된다")
    public void infoTest02() throws Exception {
        mockMvc.perform(get("/v3/department")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("request", "qa, 100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID.getCode()))
                .andExpect(jsonPath("$.message").value(INVALID.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("특수문자는 부서등록이 안된다")
    public void infoTest03() throws Exception {
        mockMvc.perform(get("/v3/department")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("request", "*, 100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID.getCode()))
                .andExpect(jsonPath("$.message").value(INVALID.getMessage()))
                .andDo(print());
    }


    @Test
    @DisplayName("부서 인원은 숫자로 이루어져있다")
    public void infoTest04() throws Exception {
        mockMvc.perform(get("/v3/department")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("request", "QA, 백이십명"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("인원수는 숫자로 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("관계구성도 테스트")
    public void relationTest01() throws Exception {
        Department department = new Department("QA", 100);
        em.persist(department);

        mockMvc.perform(get("/v3/department")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("request", "*> QA"))
                .andExpect(status().isOk())
                .andExpect(content().string("QA, 100"))
                .andDo(print());
    }

    @Test
    @DisplayName("최상위 부서 등록 테스트")
    public void relationTest02() throws Exception {
        Department department1 = new Department("A", 100);
        Department department2 = new Department("B", 100);
        Department department3 = new Department("C", 100);
        Department department4 = new Department("D", 100);
        Department department5 = new Department("E", 100);
        Department department6 = new Department("F", 100);
        Department department7 = new Department("G", 100);
        Department department8 = new Department("H", 100);
        Department department9 = new Department("I", 100);
        Department department0 = new Department("J", 100);

        em.persist(department1);
        em.persist(department2);
        em.persist(department3);
        em.persist(department4);
        em.persist(department5);
        em.persist(department6);
        em.persist(department7);
        em.persist(department8);
        em.persist(department9);
        em.persist(department0);

        String relationRequest1 = "A > B";
        String relationRequest2 = "B > C";
        String relationRequest3 = "B > D";
        String relationRequest4 = "B > E";
        String relationRequest5 = "C > F";
        String relationRequest6 = "C > G";
        String relationRequest7 = "C > H";
        String relationRequest8 = "E > I";
        String relationRequest9 = "E > J";

        mockMvc.perform(get("/v3/department").param("request", relationRequest1));
        mockMvc.perform(get("/v3/department").param("request", relationRequest2));
        mockMvc.perform(get("/v3/department").param("request", relationRequest3));
        mockMvc.perform(get("/v3/department").param("request", relationRequest4));
        mockMvc.perform(get("/v3/department").param("request", relationRequest5));
        mockMvc.perform(get("/v3/department").param("request", relationRequest6));
        mockMvc.perform(get("/v3/department").param("request", relationRequest7));
        mockMvc.perform(get("/v3/department").param("request", relationRequest8));
        mockMvc.perform(get("/v3/department").param("request", relationRequest9))
                .andExpect(status().isOk())
                .andExpect(content().string("A, 1000"))
                .andDo(print());

        String superDepartment = "* > C";
        mockMvc.perform(get("/v3/department").param("request", superDepartment))
                .andExpect(status().isOk())
                .andExpect(content().string("C, 400"))
                .andDo(print());


        Department newDepartment = new Department("K", 100);
        em.persist(newDepartment);
        String newRelation = "A > K";

        mockMvc.perform(get("/v3/department").param("request", newRelation))
                .andExpect(status().isOk())
                .andExpect(content().string("A, 700"))
                .andDo(print());

    }


}