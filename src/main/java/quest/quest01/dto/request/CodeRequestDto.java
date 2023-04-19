package quest.quest01.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import quest.quest01.type.CodeRequest;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CodeRequestDto {
//
//    @NotEmpty(message = "입력값이 없습니다.")
//    @Size(max = 30, message = "30글자 이내로 입력해주세요")
//    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "특수문자를 제외하고 입력해 주세요.")
////    private String request;


    private String requestType;

    @Size(max = 6,min = 6,message = "상점 코드는 6자리입니다.")
    @Pattern(regexp = "^[a-zA-Z]*$",message = "상점 코드는 알파벳으로 이루어져 있습니다.")
    private String storeCode;

    @Size(min = 9, max = 9, message = "상품 코드는 9자리입니다.")
    @Pattern(regexp = "^[0-9]*$", message = "상점 코드는 숫자로 이루어져 있습니다.")
    private String itemCode;

    public CodeRequestDto(CodeRequest request, String storeCode, String itemCode) {
        this.requestType = request.name();
        this.storeCode = storeCode;
        this.itemCode = itemCode;
    }
}
