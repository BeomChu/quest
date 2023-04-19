package quest.ex.customEx;

import lombok.Getter;
import quest.quest01.type.Response;

@Getter
public class CustomInvalidException extends StringIndexOutOfBoundsException {

    private Response response;

    public CustomInvalidException(Response response) {
        super(response.getMessage());
        this.response = response;
    }
}
