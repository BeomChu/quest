package quest.quest02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quest.quest02.domain.drawerItem.DrawerItem;
import quest.quest02.domain.drawerItem.DrawerItemRepository;
import quest.quest02.domain.item.Item;
import quest.quest02.domain.item.ItemRepository;
import quest.quest02.domain.drawer.Drawer;
import quest.quest02.domain.drawer.DrawerRepository;
import quest.quest02.dto.request.DrawRequest;
import quest.quest02.dto.response.DrawResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DrawService {

    private final int drawPrice = 100;

    private final DrawerRepository drawerRepository;

    private final ItemRepository itemRepository;

    private final DrawerItemRepository drawerItemRepository;


    public DrawResponseDto draw(DrawRequest request) {
        log.info("draw-start");

        Drawer drawer = new Drawer();
        drawer.charge();
        drawerRepository.save(drawer);



        log.info("drawerMoney = {}, request.count ={}", drawer.getAccount(), request.getCount());
        List<Item> itemList = new ArrayList<>();

        if (drawer.getAccount() < request.getCount() * drawPrice) {
            return new DrawResponseDto("잔액이 부족합니다. 시도 횟수를 확인해 주세요.", drawer.getAccount(), request.getLocalDateTime(), null);
        }

        int a = 0; // A상품 뽑힌 횟수
        int b = 0; // B상품 뽑힌 횟수

        for (int i = 1; i < request.getCount()+1; i++) {
            drawer.pay();
            log.info("[{}]번째 뽑기, 잔액 [{}]", i, drawer.getAccount());

            Random random = new Random();
            if (random.nextInt(10) != 0) {
                Item item = itemRepository.findById(100L + random.nextInt(7) + 1).get(); //A상품 ID '101L'~'107L'
                a++;
                itemList.add(item);

                log.info("{}상품 뽑기 성공, 상품명 = {}", item.getGrade(),item.getName());
            } else if (random.nextInt(10) == 0) {
                if (drawer.getGotB() > 2) {
                    log.info("B상품 3번뽑음 drawer.gotB = {}", drawer.getGotB());
                    continue;
                }

                drawer.gotB();
                b++;
                Item item = itemRepository.findById(200L + random.nextInt(3) + 1).get();//B상품 ID '201L'~'203L'
                itemList.add(item);
                log.info("{}상품 뽑기 성공, 상품명 = {}", item.getGrade() ,item.getName());

            } else {
                log.info("꽝");
            }

        }

        for (Item item : itemList) {
            DrawerItem drawerItem = DrawerItem.builder()
                    .drawer(drawer)
                    .item(item)
                    .build();

            drawerItemRepository.save(drawerItem);
        }


        StringBuilder sb = new StringBuilder();
        sb.append("상품뽑기 완료 ");
        sb.append(" 시도 횟수 : ").append(request.getCount());
        sb.append(" A상품 뽑은 횟수 : ").append(a);
        sb.append(" B상품 뽑은 횟수 : ").append(b);

        log.info("draw-End, result = {}", sb.toString());

        return new DrawResponseDto(sb.toString(),drawer.getAccount(), request.getLocalDateTime(), itemList);
    }


}
