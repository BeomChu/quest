package quest.quest01.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import quest.ex.customEx.CustomInvalidException;
import quest.ex.customEx.CustomNotFoundException;
import quest.quest01.domain.changer.Changer;
import quest.quest01.domain.changer.ChangerRepository;
import quest.quest01.domain.code.Code;
import quest.quest01.domain.code.CodeRepository;
import quest.quest01.dto.response.ResponseDto;
import quest.quest01.util.CodeFactory;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static quest.quest01.type.Response.*;

@SpringBootTest
@Transactional
public class CodeServiceTestV2 {

    @Autowired
    CodeService codeService;

    @Autowired
    CodeRepository codeRepository;

    @Autowired
    ChangerRepository changerRepository;

    @Autowired
    EntityManager em;


    @AfterEach
    public void after(){
        CodeFactory.page = 0;
    }

    @Test
    @DisplayName("createCodeListTest")
    public void createCodeListTest(){
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        codeService.offerCodeList(changer.getId());
        List<Code> all = codeRepository.findAll();

        assertEquals(all.size(), 10);
        assertEquals(CodeFactory.page, 1);
    }

    @Test
    @DisplayName("코드 제공시 page증가 확인")
    public void createCodeListTestForThreeTimes(){
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        codeService.offerCodeList(changer.getId());
        codeService.offerCodeList(changer.getId());
        codeService.offerCodeList(changer.getId());
        List<Code> all = codeRepository.findAll();

        assertEquals(CodeFactory.page, 3);
        assertEquals(all.size(), 30);
        assertEquals(changer.getCodeList().size(), 30);
        assertNotEquals(changer.getCodeList().get(0), changer.getCodeList().get(11));

    }

    @Test
    @DisplayName("체인저에게 코드 10개를 제공한다")
    public void offerCodeListToChanger(){
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);

        ResponseDto<?> response = codeService.offerCodeList(changer.getId());
        List<Code> data = (List<Code>) response.getData();
        System.out.println(data.size());
//        Object data = response.getData();
//        List<Code> responseCodeList = new ArrayList<>();
//        responseCodeList.addAll((List<Code>) data);

        Changer findChanger = changerRepository.findById(changer.getId()).get();
        List<Code> changerCodeList = findChanger.getCodeList();

        List<Code> all = codeRepository.findAll();


        assertEquals(response.getMessage(), "상품 코드 10개가 제공되었습니다.");
        assertEquals(changerCodeList.size(), 10);
        assertEquals(data.size(), 10);
        assertEquals(all.size(), 10);
        assertEquals(data.get(0), changerCodeList.get(0));
        assertEquals(changerCodeList.get(0).getItemCode().length(), 9);
    }

    @Test
    @DisplayName("itemCode는 숫자로 이루어진 9글자 여야 한다")
    public void checkItemCodeTestCase01() throws CustomInvalidException {
        CustomInvalidException exception1 =
                assertThrows(CustomInvalidException.class, () -> codeService.checkItemCode("1231"));
        CustomInvalidException exception2 =
                assertThrows(CustomInvalidException.class, () -> codeService.checkItemCode("123123123123"));
        CustomInvalidException exception3 =
                assertThrows(CustomInvalidException.class, () -> codeService.checkItemCode("aaabbbccc"));

        assertEquals(exception1.getMessage(), BAD_REQUEST_ITEM.getMessage());
        assertEquals(exception2.getMessage(), BAD_REQUEST_ITEM.getMessage());
        assertEquals(exception3.getMessage(), BAD_REQUEST_ITEM.getMessage());
    }

    @Test
    @DisplayName("생성되지 않은 itemCode는 교환 가능 여부를 안내할 수 없다.")
    public void checkItemCodeTestCase02(){
        CustomNotFoundException exception1 =
                assertThrows(CustomNotFoundException.class, () -> codeService.checkItemCode("123123123"));

        assertEquals(exception1.getMessage(), NOT_FOUND_ITEM.getMessage());
    }

    @Test
    @DisplayName("checkItemCode BadCase")
    public void checkItemCodeTestCase03() throws Exception {
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());

        String itemCode = changer.getCodeList().get(0).getItemCode();;

        codeService.claimItemCode("abcded", itemCode);

        em.flush();
        em.clear();

        ResponseDto<?> result = codeService.checkItemCode(itemCode);
        assertEquals(result.getMessage(), ALREADY_CHANGED.getMessage());
    }

    @Test
    @DisplayName("checkItemCode GoodCase")
    public void checkItemCodeTestCase04() throws Exception {
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());
        List<Code> codeList = (List<Code>) responseDto.getData();
        ResponseDto<?> result = codeService.checkItemCode(codeList.get(0).getItemCode());

        assertEquals(result.getMessage(), YET_CHANGED.getMessage());
    }

    @Test
    @DisplayName("상점코드에는 숫자가 들어갈 수 없고 6글자이다..")
    public void claimTest01() throws Exception {
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());
        List<Code> codeList = (List<Code>) responseDto.getData();

        String storeCode1 = "AbcdE1";
        String storeCode2 = "abcdefg";
        String storeCode3 = "KKKKZ";

        CustomInvalidException result1 = assertThrows(CustomInvalidException.class, () -> {
            codeService.claimItemCode(storeCode1, codeList.get(0).getItemCode());
        });
        CustomInvalidException result2 = assertThrows(CustomInvalidException.class, () -> {
            codeService.claimItemCode(storeCode2, codeList.get(0).getItemCode());
        });
        CustomInvalidException result3 = assertThrows(CustomInvalidException.class, () -> {
            codeService.claimItemCode(storeCode3, codeList.get(0).getItemCode());
        });


        assertEquals(result1.getMessage(), BAD_REQUEST_STORE.getMessage());
        assertEquals(result2.getMessage(), BAD_REQUEST_STORE.getMessage());
        assertEquals(result3.getMessage(), BAD_REQUEST_STORE.getMessage());
    }

    @Test
    @DisplayName("교환에 성공하면 교환된 상점 코드를 알려준다")
    public void sayStoreCode() throws Exception {
        Changer changer = new Changer("Tester");
        changerRepository.save(changer);
        ResponseDto<?> responseDto = codeService.offerCodeList(changer.getId());
        List<Code> codeList = (List<Code>) responseDto.getData();

        String storeCode = "ABCDEF";
        String itemCode = codeList.get(0).getItemCode();


        ResponseDto<?> result = codeService.claimItemCode(storeCode, itemCode);

        assertEquals(result.getCode(), SUC_CLAIM.getCode());
        assertEquals(result.getMessage(), SUC_CLAIM.getMessage());
        assertEquals(result.getData(), SUC_CLAIM.sayStoreCode(storeCode));
    }

    @Test
    @DisplayName("사용법 안내 출력")
    public void helpMessage(){
        codeService.helpMessage();
        System.out.println(codeService.helpMessage().getData());
    }



}
