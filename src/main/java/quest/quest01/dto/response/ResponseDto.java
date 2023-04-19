package quest.quest01.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import quest.quest01.type.Response;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseDto<T> {

    private int code;

    private String message;

    private T data;


    public ResponseDto(Response response, T data) {
        this.code = response.getCode();
        this.message = response.getMessage();
        this.data = data;
    }

}
