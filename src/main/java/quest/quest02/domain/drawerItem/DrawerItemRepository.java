package quest.quest02.domain.drawerItem;

import org.springframework.data.jpa.repository.JpaRepository;
import quest.quest02.domain.drawerItem.DrawerItem;

public interface DrawerItemRepository extends JpaRepository<DrawerItem, Long> {
    int countByDrawer(Long drawerId);
}
