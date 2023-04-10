package quest.quest01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import quest.quest01.domain.Code;
import quest.quest01.domain.type.Request;
import quest.quest01.domain.type.Response;
import quest.quest01.domain.type.ResponseMessage;
import quest.quest01.service.CodeService;
import quest.quest01.util.CodeFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class CodeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CodeService codeService;

    @Test
    @DisplayName("helpMessage")
    public void help() throws Exception {
        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"request\" : \"help\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseMessage.HelpMessage.code()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.HelpMessage.message()))
                .andExpect(jsonPath("$.data").value(ResponseMessage.HelpMessage.helpMessage()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("checkItemCode")
    public void checkItemBadCase() throws Exception {
        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"request\" : \"check123123123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseMessage.NotFoundItem.code()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.NotFoundItem.message()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("checkItemCode")
    public void checkItemGoodCase() throws Exception {
        codeService.createCodeList();

        Page<Code> codeList = codeService.getCodeList();
        Code code = codeList.getContent().get(0);

        String itemCode = code.getItemCode();

        Map<String, String> request = new HashMap<>();
        request.put("request" , Request.CHECK + itemCode);

        ObjectMapper om = new ObjectMapper();
        String requestJson = om.writeValueAsString(request);


        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseMessage.FindItemCode.code()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.FindItemCode.message()))
                .andExpect(jsonPath("$.data.itemCode").value(itemCode)) //data
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("claimItemCode")
    public void claimItemGoodCase() throws Exception {
        codeService.createCodeList();

        Page<Code> codeList = codeService.getCodeList();
        Code code = codeList.getContent().get(0);

        String itemCode = code.getItemCode();
        String storeCode = CodeFactory.storeCodeFactory();

        Map<String, String> request = new HashMap<>();
        request.put("request", Request.CLAIM + storeCode + itemCode);

        ObjectMapper om = new ObjectMapper();
        String requestJson = om.writeValueAsString(request);


        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseMessage.SucClaim.code()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SucClaim.message()))
                .andExpect(jsonPath("$.data").value(ResponseMessage.SucClaim.sayStoreCode(storeCode))) //data
                .andDo(MockMvcResultHandlers.print());
    }

}