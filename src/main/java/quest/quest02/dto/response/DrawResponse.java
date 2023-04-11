package quest.quest02.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import quest.quest02.domain.item.Item;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DrawResponse {
    private String message;

    private LocalDateTime localDateTime;

    private Item item;

    public DrawResponse(String message,LocalDateTime drawTime, Item item) {
        this.message = message;
        this.localDateTime = drawTime;
        this.item = item;
    }
}
