package quest.quest03.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepartmentInfo {

    private String department;
    private int count;

    public DepartmentInfo(String department, int count) {
        this.department = department;
        this.count = count;
    }
}
