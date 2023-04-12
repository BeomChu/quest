package quest.quest03.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import quest.quest03.dto.request.DepartmentInfo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Department {

    @Id @GeneratedValue
    public Long id;

    private String name;

    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department parent;

    @OneToMany(mappedBy = "parent")
    private List<Department> children = new ArrayList<>();

    public Department(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public void addChildren(Department child) {
        this.children.add(child);
        child.setParent(this);
    }

    private void setParent(Department parent) {
        this.parent = parent;
    }


    public Department editInfo(DepartmentInfo info) {
        this.name = info.getDepartment();
        this.count = info.getCount();
        return this;
    }
}


