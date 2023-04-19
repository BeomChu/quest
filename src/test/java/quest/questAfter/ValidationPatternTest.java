package quest.questAfter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ValidationPatternTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper om = new ObjectMapper();

//    @Test
//    @DisplayName("입력값이 없으면 안된다")
//    public void testCase00() throws Exception {
//        Map<String, String> request = new HashMap<>();
//        request.put("request", "");
//        String body = om.writeValueAsString(request);
//
//        Map<String, String> result = new HashMap<>();
//        result.put("request", "입력값이 없습니다.");
//
//        mockMvc.perform(get("/pattern/test")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(content().string(result.toString()))
//                .andDo(MockMvcResultHandlers.print());
//    }
//
//
//
//    @Test
//    @DisplayName("입력값에 공백이 포함된다")
//    public void testCase01() throws Exception {
//        Map<String, String> request = new HashMap<>();
//        request.put("request", "QA DEV 001231232323");
//        String body = om.writeValueAsString(request);
//
//        mockMvc.perform(get("/pattern/test")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(body))
//                .andExpect(content().string("ok"))
//                .andDo(MockMvcResultHandlers.print());
//    }
//
//    @Test
//    @DisplayName("특수문자는 포함되면 안된다")
//    public void testCase02() throws Exception {
//        Map<String, String> request = new HashMap<>();
//        request.put("request", "#$@%%%%$QA DEV");
//        String body = om.writeValueAsString(request);
//
//        Map<String, String> result = new HashMap<>();
//        result.put("request", "특수문자를 제외하고 입력해 주세요.");
//
//        mockMvc.perform(get("/pattern/test")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(content().string(result.toString()))
//                .andDo(MockMvcResultHandlers.print());
//    }
//
//    @Test
//    @DisplayName("요청값은 30글자가 넘을 수 없다")
//    public void testCase03() throws Exception {
//        Map<String, String> request = new HashMap<>();
//        request.put("request", "0123456789012345678901234567890123");
//        String body = om.writeValueAsString(request);
//
//        Map<String, String> result = new HashMap<>();
//        result.put("request", "30글자 이내로 입력해주세요");
//
//        mockMvc.perform(get("/pattern/test")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(content().string(result.toString()))
//                .andDo(MockMvcResultHandlers.print());
//    }


    @Test
    @DisplayName("특수문자가 포함되면 CustomValidationException이 호출된다")
    public void testCase04() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("request", "#$@%%%%$QA DEV");
        String body = om.writeValueAsString(request);

        mockMvc.perform(get("/pattern/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("입력된 요청은 잘못된 요청입니다."))
                .andExpect(jsonPath("$.data.request").value("특수문자를 제외하고 입력해 주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("입력값이 없으면 안된다")
    public void testCase05() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("request", "");
        String body = om.writeValueAsString(request);

        Map<String, String> result = new HashMap<>();
        result.put("request", "입력값이 없습니다.");

        mockMvc.perform(get("/pattern/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("입력된 요청은 잘못된 요청입니다."))
                .andExpect(jsonPath("$.data.request").value("입력값이 없습니다."))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @DisplayName("요청값은 30글자가 넘을 수 없다")
    public void testCase03() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("request", "0123456789012345678901234567890123");
        String body = om.writeValueAsString(request);

        mockMvc.perform(get("/pattern/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("입력된 요청은 잘못된 요청입니다."))
                .andExpect(jsonPath("$.data.request").value("30글자 이내로 입력해주세요"))
                .andDo(MockMvcResultHandlers.print());
    }





}