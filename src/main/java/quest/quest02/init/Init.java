package quest.quest02.init;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import quest.quest02.domain.item.Item;
import quest.quest02.domain.item.ItemRepository;
import quest.quest02.domain.type.Grade;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.logging.Logger;


/**
 * 개발팀 요구사항
 *
 * 1. 상품은 총 10 종류를 개발자가 임의로 제공합니다.
 * 상품종류의 표현은 다음과 같습니다.
 * [상품종류],[등급],[유통기한]
 *
 * 비지니스 팀 요구사항
 *
 * 11. A, B 등급의 상품은 최소 2종류 이상 준비합니다.
 */
@Component
@Transactional
@RequiredArgsConstructor
public class Init {

    private final ItemRepository itemRepository;

    @PostConstruct
    public void init() {
        Item item1 = new Item(201L, "Chicken", Grade.B, LocalDateTime.now().plusDays(14));
        Item item2 = new Item(202L, "Pizza", Grade.B, LocalDateTime.now().plusDays(7));
        Item item3 = new Item(203L, "Steak", Grade.B, LocalDateTime.now().minusDays(3));
        Item item4 = new Item(101L, "Americano", Grade.A, LocalDateTime.now().plusDays(14));
        Item item5 = new Item(102L, "Latte", Grade.A, LocalDateTime.now().plusDays(7));
        Item item6 = new Item(103L, "Chocolate", Grade.A, LocalDateTime.now().minusDays(3));
        Item item7 = new Item(104L, "Water", Grade.A, LocalDateTime.now().plusDays(14));
        Item item8 = new Item(105L, "Snack", Grade.A, LocalDateTime.now().plusDays(7));
        Item item9 = new Item(106L, "IceCream", Grade.A, LocalDateTime.now().minusDays(3));
        Item item0 = new Item(107L, "ColdBrew", Grade.A, LocalDateTime.now().plusDays(14));

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);
        itemRepository.save(item5);
        itemRepository.save(item6);
        itemRepository.save(item7);
        itemRepository.save(item8);
        itemRepository.save(item9);
        itemRepository.save(item0);
    }
}
