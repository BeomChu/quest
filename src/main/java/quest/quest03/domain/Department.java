package quest.quest03.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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



    public void removeParent(Department parent) {
        if (this.parent != null) {
            this.parent = null;
            parent.getChildren().remove(this);
        }
    }
}


