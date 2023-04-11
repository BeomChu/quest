package quest.quest02.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 2. DRAW 함수 호출시에는 원하는 "뽑기" 횟수와 시도한 시각을 파라미터로 입력 받습니다. 시각은 유통기한과 연관성이 있습니다.
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DrawRequest {

    private int count;

    private LocalDateTime localDateTime;

    public DrawRequest(int count) {
        this.count = count;
        this.localDateTime = LocalDateTime.now();
    }
}
