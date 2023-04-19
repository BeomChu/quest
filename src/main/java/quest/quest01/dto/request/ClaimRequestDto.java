package quest.quest01.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClaimRequestDto {

    @Size(min = 6, max = 6, message = "상점 코드는 6글자로 이루어져 있습니다.")
    @Pattern(regexp = "^[a-zA-z]*$]", message = "상점 코드는 영어로 이루어져 있습니다.")
    private String storeCode;
    private String itemCode;

    public ClaimRequestDto(String storeCode, String itemCode) {
        this.storeCode = storeCode;
        this.itemCode = itemCode;
    }
}
