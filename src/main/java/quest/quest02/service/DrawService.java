package quest.quest02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quest.quest02.domain.item.Item;
import quest.quest02.domain.item.ItemRepository;
import quest.quest02.domain.member.Member;
import quest.quest02.domain.member.MemberRepository;
import quest.quest02.dto.request.DrawRequest;
import quest.quest02.dto.response.DrawResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DrawService {

    private final int drawPrice = 100;

    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;

    public void charge(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found Drawer"));

        member.charge();
    }

    public List<DrawResponse> draw(Long memberId, DrawRequest request) {
        log.info("draw-start");

        Member drawer = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found Drawer"));
        log.info("drawerMoney = {}, request.count ={}", drawer.getAccount(), request.getCount());
        List<DrawResponse> resultList = new ArrayList<>();

        if (drawer.getAccount() < request.getCount() * drawPrice) { // for문 밖으로 나옴
            resultList.add( new DrawResponse("잔액 부족", null, null));
            return resultList;
        }

        for (int i = 0; i < request.getCount(); i++) {
            //잔액 확인

            drawer.pay(); // 잔액 차감
            log.info("[{}]번째 뽑기, 잔액 [{}]", i+1, drawer.getAccount());

            Random random = new Random();
            if (random.nextInt(10) != 0) { //90% 확률
                Item item = itemRepository.findById(100L + random.nextInt(7) + 1)// A상품 Id 101~107
                        .orElseThrow(() -> new IllegalStateException("뭔가 잘못됨."));

                log.info("{}상품 뽑기 성공, 상품명 = {}", item.getGrade(),item.getName());
                resultList.add(new DrawResponse("A상품 뽑기 성공",request.getLocalDateTime(),  item));
            } else if (random.nextInt(10) == 0) { //10%확률

                if (drawer.getGotB() > 2) { //B를 3번 뽑았다면
                    log.info("B상품 3번뽑음 drawer.gotB{}", drawer.getGotB());
                    resultList.add(new DrawResponse("더이상 B상품을 뽑을 수 없습니다.", null,null));
                }

                drawer.gotB(); // member.gotB++
                Item item = itemRepository.findById(200L + random.nextInt(3) + 1) //B상품 id 201~203
                        .orElseThrow(() -> new IllegalStateException("뭔가 잘못됨."));

                log.info("{}상품 뽑기 성공, 상품명 = {}", item.getGrade(),item.getName());
                resultList.add(new DrawResponse(
                        "B상품 뽑기 성공, 현재 뽑은 B상품 갯수 : " + drawer.getGotB(),
                        request.getLocalDateTime(),
                        item));
            } else { //90% 확인 후 10% 확인 후
                log.info("꽝");
                resultList.add(new DrawResponse("꽝", request.getLocalDateTime(), null));
            }

        }

        log.info("draw-End");
        return resultList;
    }


}
