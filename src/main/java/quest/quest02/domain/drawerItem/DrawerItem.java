package quest.quest02.domain.drawerItem;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import quest.quest02.domain.drawer.Drawer;
import quest.quest02.domain.item.Item;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DrawerItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drawer_id")
    private Drawer drawer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder

    public DrawerItem(Drawer drawer, Item item) {
        this.drawer = drawer;
        this.item = item;
    }
}
