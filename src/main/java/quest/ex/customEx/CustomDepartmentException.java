package quest.ex.customEx;

import lombok.Getter;
import quest.quest01.type.Response;

@Getter
public class CustomDepartmentException extends RuntimeException{

    private Response response;

    public CustomDepartmentException(Response response) {
        super(response.getMessage());
        this.response = response;

    }
}
