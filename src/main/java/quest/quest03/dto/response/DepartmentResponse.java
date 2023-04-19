package quest.quest03.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class DepartmentResponse{

    private String department;
    private String count;

    public DepartmentResponse(String department, String count) {
        this.department = department;
        this.count = count;
    }
}
