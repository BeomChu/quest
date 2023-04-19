package quest.quest01.domain.changer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import quest.quest01.domain.code.Code;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Changer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "itemCode")
    private List<Code> codeList = new ArrayList<>();

    public void offerCodeList(List<Code> codeList){
        for (Code code : codeList) {
            this.codeList.add(code);
        }
    }

    public Changer(String name) {
        this.name = name;
    }
}
