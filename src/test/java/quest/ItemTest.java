package quest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import quest.quest02.domain.item.Item;
import quest.quest02.domain.type.Grade;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest
public class ItemTest {

    @Autowired
    EntityManager em;

    @Test
    public void test(){
        Item item = new Item(1L,"chicken", Grade.B, LocalDateTime.now().minusDays(3));
        em.persist(item);
        em.flush();
    }


}
