package quest.questAfter;

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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@AutoConfigureMockMvc
@SpringBootTest
public class ConverterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("requestGetTest")
    public void getParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/prac/param?relation=A>B"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("requestPostTest")
    public void postParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/prac/param?relation=A>B"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("requestBodyTest")
    public void getBody() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("parent", "A");
        body.put("child", "B");
        ObjectMapper om = new ObjectMapper();


        mockMvc.perform(MockMvcRequestBuilders.get("/prac/body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(om.writeValueAsString(body)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("requestBodyTest")
    public void postBody() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("parent", "A");
        body.put("child", "B");
        ObjectMapper om = new ObjectMapper();


        mockMvc.perform(MockMvcRequestBuilders.get("/prac/body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(om.writeValueAsString(body)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("QuestReqTest")
    public void getBodyReq() throws Exception{
        Map<String, String> body = new HashMap<>();
        body.put("request", "A>B");
        ObjectMapper om = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.get("/prac/req")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(om.writeValueAsString(body)))
                .andDo(MockMvcResultHandlers.print());
    }

}
