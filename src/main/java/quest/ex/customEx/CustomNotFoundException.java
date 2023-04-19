package quest.ex.customEx;


import lombok.Getter;
import quest.quest01.type.Response;

@Getter
public class CustomNotFoundException extends RuntimeException{

    private Response response;

    public CustomNotFoundException(Response response) {
        super(response.getMessage());
        this.response = response;
    }

}
