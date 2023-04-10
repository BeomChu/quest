package quest.quest01.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class CodeFactoryTest {

    @Test
    @DisplayName("1. 코드 생성 테스트")
    public void itemCodeLengthTest() {
        String itemCode = CodeFactory.itemCodeFactory();
        String storeCode = CodeFactory.storeCodeFactory();
        assertEquals(itemCode.length(), 9);
        assertEquals(storeCode.length(), 6);
    }
}
