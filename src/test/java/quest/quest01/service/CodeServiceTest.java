package quest.quest01.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import quest.quest01.domain.Code;
import quest.quest01.domain.CodeRepository;
import quest.quest01.domain.type.Request;
import quest.quest01.domain.type.Response;
import quest.quest01.domain.type.ResponseMessage;
import quest.quest01.dto.response.ResponseDto;
import quest.quest01.util.CodeFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static quest.quest01.domain.type.Request.*;
import static quest.quest01.domain.type.ResponseMessage.*;

@SpringBootTest
@Transactional
class CodeServiceTest {

    @Autowired
    CodeService codeService;

    @Autowired
    CodeRepository codeRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void before() {
        codeService.createCodeList();
    }

    @Test
    @DisplayName("CodeListTest")
    public void codeList(){
        Page<Code> codeList = codeService.getCodeList();
        List<Code> findAll = codeRepository.findAll();

        assertEquals(findAll.size(), 20);
        assertEquals(codeList.getContent().size(), 10);
    }

    @Test
    @DisplayName("checkItemCase01")
    public void checkItemCase01() {
        Page<Code> codeList = codeService.getCodeList();
        Code code = codeList.getContent().get(0);
        System.out.println(code.getItemCode());
        String request = "check123123123";

        ResponseDto<?> response1 = codeService.checkItemCode(request);

        assertEquals(response1.getMessage(), NotFoundItem.message());
    }


    @Test
    @DisplayName("checkItemCase02")
    public void checkItemCase02() {
        Page<Code> codeList = codeService.getCodeList();
        Code code = codeList.getContent().get(0);

        String request = "check" + code.getItemCode();
        System.out.println(request);
        ResponseDto<?> response2 = codeService.checkItemCode(request);

        assertEquals(response2.getMessage(), FindItemCode.message());
    }

    @Test
    @DisplayName("checkItemCase03")
    public void checkItemCase03(){
        Page<Code> codeList = codeService.getCodeList();
        Code code = codeList.getContent().get(1);
        String request = "check" + code.getItemCode();

        String storeCode = CodeFactory.storeCodeFactory();
        System.out.println("In Test call code.changeItem(storeCode)");
        code.changeItem(storeCode);
        System.out.println("In Test code.storeCode = " + code.getStoreCode());

        em.flush();

        ResponseDto<?> response3 = codeService.checkItemCode(request);

        assertEquals(response3.getMessage(), AlreadyChanged.message());
        assertEquals(response3.getData(), AlreadyChanged.sayStoreCode(storeCode));
    }

    @Test
    @DisplayName("checkItemCase04")
    public void checkItemCase04(){
        ResponseDto<?> response4 = codeService.checkItemCode(null);

        assertEquals(response4.getMessage(), BadRequest.message());
    }

    @Test
    @DisplayName("claimTest")
    public void claimTestCase01(){
        Page<Code> codeList = codeService.getCodeList();
        String storeCode = CodeFactory.storeCodeFactory();
        String request = CHECK + storeCode + codeList.getContent().get(0).getItemCode();

        ResponseDto<?> responseDto = codeService.claimItemCode(request);
        assertEquals(responseDto.getCode(), SucClaim.code());
        assertEquals(responseDto.getMessage(), SucClaim.message());
        assertEquals(responseDto.getData(), SucClaim.sayStoreCode(storeCode));
    }


    @Test
    @DisplayName("Help")
    public void helpMessage(){
        System.out.println(HELP.helpMessage());
    }


}