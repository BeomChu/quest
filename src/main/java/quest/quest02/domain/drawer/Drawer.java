package quest.quest02.domain.drawer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import quest.quest02.domain.drawerItem.DrawerItem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Getter
public class Drawer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drawer_id")
    public Long id;

    private int account = 0;

    private int gotB = 0;

    @OneToMany(mappedBy = "drawer", cascade = CascadeType.ALL)
    private List<DrawerItem> itemList = new ArrayList<>();

    public void charge(){
        this.account += 10000;
        log.info("member.charge() 호출, 충전 후 잔액 ={} ",this.account);
    }


    public void pay(){
        this.account -= 100;
    }

    public void gotB(){
        this.gotB++;
    }
}
