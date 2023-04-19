package quest.quest02.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import quest.quest02.domain.drawer.DrawerRepository;

import javax.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DrawControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    DrawerRepository drawerRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("뽑기 횟수는 100번 이상일 수 없다")
    public void noMoreThanOneHundred() throws Exception {

        String requestCount = String.valueOf(101);
        String requestTime = LocalDateTime.now().toString();


        mockMvc.perform(MockMvcRequestBuilders.get("/v2/draw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("count", requestCount)
                        .param("localDateTime", requestTime))
                .andExpect(jsonPath("$.message").value("잔액이 부족합니다. 시도 횟수를 확인해 주세요."))
                .andExpect(jsonPath("$.localDateTime").value(requestTime))
                .andExpect(jsonPath("$.itemList").isEmpty())
                .andDo(print());
    }


    @Test
    @DisplayName("100번뽑기")
    public void test() throws Exception {
        String requestCount = String.valueOf(100);
        String requestTime = LocalDateTime.now().toString();


        mockMvc.perform(MockMvcRequestBuilders.get("/v2/draw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("count", requestCount)
                        .param("localDateTime", requestTime))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.localDateTime").value(requestTime))
                .andDo(print())
                .andReturn();
    }
}


