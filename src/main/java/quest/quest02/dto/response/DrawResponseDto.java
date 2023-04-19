package quest.quest02.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import quest.quest02.domain.item.Item;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DrawResponseDto {
    private String message;

    private int balance;

    private LocalDateTime localDateTime;

    private List<Item> itemList;

    public DrawResponseDto(String message, int balance, LocalDateTime localDateTime, List<Item> itemList) {
        this.message = message;
        this.balance = balance;
        this.localDateTime = localDateTime;
        this.itemList = itemList;
    }
}
