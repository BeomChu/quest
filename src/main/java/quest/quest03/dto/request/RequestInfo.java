package quest.quest03.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class RequestInfo {
    private String departmentName;

    public RequestInfo(String departmentName) {
        this.departmentName = departmentName;
    }
}
