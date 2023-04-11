package quest.quest02.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import quest.quest02.domain.member.Member;
import quest.quest02.domain.member.MemberRepository;
import quest.quest02.dto.request.DrawRequest;
import quest.quest02.dto.response.DrawResponse;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DrawServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    DrawService drawService;

    @Test
    @DisplayName("잔액 충전 테스트")
    public void chargeTest(){
        Member member = new Member();
        memberRepository.save(member);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId()).orElseThrow(()-> new IllegalArgumentException("뭔가 잘못됨"));
        drawService.charge(findMember.id);

        assertEquals(findMember.getAccount(), 10000);
    }

    @Test
    @DisplayName("Member 미 생성시 뽑기 불가능")
    public void noMemberDraw() {
        DrawRequest request = new DrawRequest(2);

        assertThrows(IllegalArgumentException.class, ()-> drawService.draw(1L ,request));
    }

    @Test
    @DisplayName("잔액 부족시 뽑기 불가능")
    public void noMoneyTest() {
        Member member = new Member();

        memberRepository.save(member);

        em.flush();
        em.clear();

        DrawRequest request = new DrawRequest(2);

        List<DrawResponse> result = drawService.draw(member.getId(), request);
        assertEquals(result.get(0).getMessage(), "잔액 부족");
    }

    @Test
    @DisplayName("잔액보다 횟수가 많으면 잔액부족함.")
    public void allOfMoneyDraw() {
        Member member = new Member();
        member.charge();

        memberRepository.save(member);

        em.flush();
        em.clear();

        DrawRequest request = new DrawRequest(101);

        List<DrawResponse> result = drawService.draw(member.getId(), request);
        assertEquals(result.get(0).getMessage(), "잔액 부족");
    }

    @Test
    @DisplayName("100번뽑고 로그찍기")
    public void drawForLog() {
        Member member = new Member();
        member.charge();

        memberRepository.save(member);

        em.flush();
        em.clear();

        DrawRequest request = new DrawRequest(100);

        List<DrawResponse> result = drawService.draw(member.getId(), request);
        for (DrawResponse drawResponse : result) {
            System.out.println("drawResponse.toString() = " + drawResponse.toString());
        }
        assertEquals(result.size(), 100);
    }


}