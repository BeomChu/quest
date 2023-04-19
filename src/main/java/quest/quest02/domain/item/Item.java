package quest.quest02.domain.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quest.quest02.domain.type.Grade;

import javax.persistence.*;
import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @Column(name = "item_id")
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Grade grade;


    private LocalDateTime expirationDate;

    /**
     * 고객의 입력
     * 2. DRAW 함수 호출시에는 원하는 "뽑기" 횟수와 시도한 시각을 파라미터로 입력 받습니다. 시각은 유통기한과 연관성이 있습니다.
     * 6. 상품은 유통기한이 있습니다. 유통기한이 지난 상품은 고객에게 제공할 수 없습니다.
     */
    public boolean isExpired(LocalDateTime drawTime) {
        log.info("itemDate, drawTime = [{}][{}]", this.expirationDate, drawTime);
        return this.expirationDate.isBefore(drawTime);
    }

    public Item(Long id, String name, Grade grade, LocalDateTime expirationDate) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.expirationDate = expirationDate;
    }
}
