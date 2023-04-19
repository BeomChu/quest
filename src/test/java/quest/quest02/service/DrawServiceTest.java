package quest.quest02.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.AbstractAuditable_;
import quest.quest02.domain.drawer.Drawer;
import quest.quest02.domain.drawer.DrawerRepository;
import quest.quest02.domain.drawerItem.DrawerItem;
import quest.quest02.dto.request.DrawRequest;
import quest.quest02.dto.response.DrawResponseDto;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DrawServiceTest {

    @Autowired
    DrawService drawService;

    @Autowired
    DrawerRepository drawerRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("200원을 고객이 사용하면 2번의 뽑기 기회가 제공된다, 그리고 돈이 차감된다")
    public void drawTest01() {
        DrawRequest drawRequest = new DrawRequest(2, LocalDateTime.now());
        DrawResponseDto response = drawService.draw(drawRequest);


        List<Drawer> resultList = em.createQuery("select d from Drawer d join fetch d.itemList", Drawer.class)
                .getResultList();

        int size = resultList.get(0).getItemList().size();
        assertEquals(size,2);
        assertEquals(response.getBalance(), 9800);
    }

    @Test
    @DisplayName("B상품은 3번만 뽑을 수 있다")
    public void gotBItem(){
        DrawRequest drawRequest = new DrawRequest(100, LocalDateTime.now());
        DrawResponseDto response = drawService.draw(drawRequest);

        List<DrawerItem> resultList =
                em.createQuery("select di from DrawerItem di where di.item.id > 200L", DrawerItem.class)
                .getResultList(); //B상품 id = 201L ~ 203L

        boolean check = resultList.size() <= 3;
        assertTrue(check);


    }

}