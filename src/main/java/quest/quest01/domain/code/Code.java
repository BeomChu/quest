package quest.quest01.domain.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quest.quest01.util.CodeFactory;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Random;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Code {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemCode;

    private String storeCode;

    /**
     * 생성자 호출시 CodeFactory.itemCodeFactory로 생성되던 코드를 외부로 이동
     * isChanged필드 삭제 -> isChagned()함수로 변경
     * @param itemCode
     */
    public Code(String itemCode) {
        this.itemCode = itemCode;
    }


    public boolean isChanged() {
        return storeCode != null;
    }

    public void changeItem(String storeCode){
        this.storeCode = storeCode;
        log.info("response chnageItem, request.storeCode = [{}], this.storeCode = [{}]", storeCode, this.storeCode);
    }

}
