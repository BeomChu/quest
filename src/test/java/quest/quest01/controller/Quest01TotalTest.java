package quest.quest01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import quest.quest01.domain.changer.Changer;
import quest.quest01.domain.changer.ChangerRepository;
import quest.quest01.domain.code.Code;
import quest.quest01.domain.code.CodeRepository;
import quest.quest01.dto.response.ResponseDto;
import quest.quest01.service.CodeService;
import quest.quest01.util.CodeFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static quest.quest01.type.Response.*;
import static quest.quest01.util.HelpMessage.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class Quest01TotalTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CodeService codeService;

    @Autowired
    ChangerRepository changerRepository;

    @Autowired
    CodeRepository codeRepository;

    String storeCode = "AbcdEf";

    ObjectMapper om = new ObjectMapper();

    @AfterEach
    public void after(){
        CodeFactory.page = 0;
    }


    @Test
    @DisplayName("1-1.고객이 상품 교환을 요구하면 가능한지 여부와 교환 결과를 안내해 주세요.(상품 등록 안됨)")
    public void claimRequestTest01() throws Exception {

        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "claim"+ storeCode +" 123 123 123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND_ITEM.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_FOUND_ITEM.getMessage()))
                .andExpect(jsonPath("$.data").value(helpMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("1-2.고객이 상품 교환을 요구하면 가능한지 여부와 교환 결과를 안내해 주세요.(이미 교환됨)")
    public void  claimRequestTest02() throws Exception {
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());
        List<Code> data = (List<Code>) responseDto.getData();
        codeService.claimItemCode(storeCode, data.get(0).getItemCode());


        ResultActions request = mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "claim" + storeCode + data.get(0).getItemCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ALREADY_CHANGED.getCode()))
                .andExpect(jsonPath("$.message").value(ALREADY_CHANGED.getMessage()))
                .andExpect(jsonPath("$.data").value(ALREADY_CHANGED.sayStoreCode(storeCode)))
                .andDo(print());
    }

    @Test
    @DisplayName("1-3.고객이 상품 교환을 요구하면 가능한지 여부와 교환 결과를 안내해 주세요.(잘못된 요청)")
    public void  claimRequestTest03() throws Exception {
        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "claIm ABcde 1231 2312 123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID.getCode()))
                .andExpect(jsonPath("$.message").value(INVALID.getMessage()))
                .andExpect(jsonPath("$.data").value(helpMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("1-4.고객이 상품 교환을 요구하면 가능한지 여부와 교환 결과를 안내해 주세요.(정상)")
    public void  claimRequestTest04() throws Exception {
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());
        List<Code> data = (List<Code>) responseDto.getData();
        String itemCode = data.get(0).getItemCode();

        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "claim" + storeCode + itemCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SUC_CLAIM.getCode()))
                .andExpect(jsonPath("$.message").value(SUC_CLAIM.getMessage()))
                .andExpect(jsonPath("$.data").value(SUC_CLAIM.sayStoreCode(storeCode)))
                .andDo(print());
    }

    @Test
    @DisplayName("2. 상품 코드는 10개가 준비되면 고객에게 10개까지만 제공됩니다.")
    public void codeLengthTest(){
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());

        List<Code> data = (List<Code>) responseDto.getData();
        Code code = data.get(0);

        List<Code> all = codeRepository.findAll();

        assertEquals(code.getItemCode().length(), 9);
        assertEquals(all.size(), 10);
        assertEquals(data.size(), 10);
    }

    @Test
    @DisplayName("3.상품 코드는 0~9 자연수 글자로 이루어져 있으며 9문자로 이루어져 있습니다.")
    public void validItemCode(){
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());

        assertEquals(changer.getCodeList().get(0).getItemCode().length() , 9);
        assertEquals(changer.getCodeList().get(1).getItemCode().length() , 9);
        assertEquals(changer.getCodeList().get(2).getItemCode().length() , 9);
        assertEquals(changer.getCodeList().get(3).getItemCode().length() , 9);
        assertEquals(changer.getCodeList().get(4).getItemCode().length() , 9);
        assertEquals(changer.getCodeList().get(5).getItemCode().length() , 9);
        assertEquals(changer.getCodeList().get(6).getItemCode().length() , 9);
        assertEquals(changer.getCodeList().get(7).getItemCode().length() , 9);
        assertEquals(changer.getCodeList().get(8).getItemCode().length() , 9);
        assertEquals(changer.getCodeList().get(9).getItemCode().length() , 9);
    }

    @Test
    @DisplayName("4.상품 코드를 이용하여 상품 교환이 1번 이루어지면, 다시 해당 상품 코드로는 상품 교환을 할 수 없습니다.")
    public void claimDuplicationTest() throws Exception {
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());

        String itemCode = changer.getCodeList().get(0).getItemCode();
        String storeCode = "aBcdEF";

        codeService.claimItemCode(storeCode, itemCode);

        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "claim" + storeCode + itemCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ALREADY_CHANGED.getCode()))
                .andExpect(jsonPath("$.message").value(ALREADY_CHANGED.getMessage()))
                .andExpect(jsonPath("$.data").value(ALREADY_CHANGED.sayStoreCode(storeCode)))
                .andDo(print());
    }

    @Test
    @DisplayName("5-1 고객은 상품 코드를 사용하기 전에 미리 상품을 교환할 수 있는지 확인이 가능합니다. (등록되지 않음)")
    public void checkItemCodeTest01() throws Exception {
        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "check" + "123123123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND_ITEM.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_FOUND_ITEM.getMessage()))
                .andExpect(jsonPath("$.data").value(helpMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("5-2 고객은 상품 코드를 사용하기 전에 미리 상품을 교환할 수 있는지 확인이 가능합니다. (잘못된 요청)")
    public void checkItemCodeTest02() throws Exception {
        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "check" + storeCode + "123123123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID.getCode()))
                .andExpect(jsonPath("$.message").value(INVALID.getMessage()))
                .andExpect(jsonPath("$.data").value(helpMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("5-3 고객은 상품 코드를 사용하기 전에 미리 상품을 교환할 수 있는지 확인이 가능합니다. (정상)")
    public void checkItemCodeTest03() throws Exception {
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());

        String itemCode = changer.getCodeList().get(0).getItemCode();
        String data = om.writeValueAsString(changer.getCodeList().get(0));

        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "check" + itemCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(YET_CHANGED.getCode()))
                .andExpect(jsonPath("$.message").value(YET_CHANGED.getMessage()))
                .andExpect(jsonPath("$.data.itemCode").value(itemCode))
                .andDo(print());
    }
    @Test
    @DisplayName("6. HARETREATS는 고객에게 CHECK, HELP, CLAIM 명령어를 사용할 수 있게 합니다." +
            "각 키워드는 CHECK(상품 교환여부 확인), HELP(사용법 안내), CLAIM(상품 교환) 을 의미합니다.")
    public void requestList() throws Exception {
        //check
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());

        String itemCode = changer.getCodeList().get(0).getItemCode();

        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "check" + itemCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(YET_CHANGED.getCode()))
                .andExpect(jsonPath("$.message").value(YET_CHANGED.getMessage()))
                .andExpect(jsonPath("$.data.itemCode").value(itemCode))
                .andDo(print());

        //claim
        codeService.claimItemCode(storeCode, itemCode);


        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "claim" + storeCode + itemCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ALREADY_CHANGED.getCode()))
                .andExpect(jsonPath("$.message").value(ALREADY_CHANGED.getMessage()))
                .andExpect(jsonPath("$.data").value(ALREADY_CHANGED.sayStoreCode(storeCode)))
                .andDo(print());


        //help
        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "help" ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HELP_MESSAGE.getCode()))
                .andExpect(jsonPath("$.message").value(HELP_MESSAGE.getMessage()))
                .andExpect(jsonPath("$.data").value(helpMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("7. SHARETREATS 는 상품 코드 목록을 준비하고 고객은 상품 코드내에서만 상품 교환이 가능합니다.")
    public void setCodeListAndChange() throws Exception {
        //before create codeList
        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "check" + "123123123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND_ITEM.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_FOUND_ITEM.getMessage()))
                .andExpect(jsonPath("$.data").value(helpMessage()))
                .andDo(print());

        //after create
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());
        List<Code> data = (List<Code>) responseDto.getData();
        String itemCode = data.get(0).getItemCode();

        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "claim" + storeCode + itemCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SUC_CLAIM.getCode()))
                .andExpect(jsonPath("$.message").value(SUC_CLAIM.getMessage()))
                .andExpect(jsonPath("$.data").value(SUC_CLAIM.sayStoreCode(storeCode)))
                .andDo(print());
    }

    @Test
    @DisplayName("8. 상품 교환을 할 때는 어떤 상점에서 교환하였는지 상점 코드를 알아야 합니다.")
    public void findStoreCode() throws Exception {
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());
        List<Code> data = (List<Code>) responseDto.getData();
        String itemCode = data.get(0).getItemCode();

        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "claim" + storeCode + itemCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SUC_CLAIM.getCode()))
                .andExpect(jsonPath("$.message").value(SUC_CLAIM.getMessage()))
                .andExpect(jsonPath("$.data").value(SUC_CLAIM.sayStoreCode(storeCode)))
                .andDo(print());

        assertEquals(data.get(0).getStoreCode(), storeCode);
    }

    @Test
    @DisplayName("9. 상점 코드는 A~Z,a~z 까지의 대,소 영문자만 사용이 가능하며 6문자로 이루어져 있습니다.")
    public void storeCodePatternTest() throws Exception {
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());
        List<Code> data = (List<Code>) responseDto.getData();
        String itemCode = data.get(0).getItemCode();

        mockMvc.perform(get("/v1/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("request", "claim" + storeCode + itemCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SUC_CLAIM.getCode()))
                .andExpect(jsonPath("$.message").value(SUC_CLAIM.getMessage()))
                .andExpect(jsonPath("$.data").value(SUC_CLAIM.sayStoreCode(storeCode)))
                .andDo(print());

        assertEquals(data.get(0).getStoreCode(), storeCode);
        assertTrue(Pattern.matches("^[a-zA-Z]*$", data.get(0).getStoreCode()));
        assertEquals(data.get(0).getStoreCode().length(), 6);
    }


}
