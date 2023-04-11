package quest.quest02.domain.member;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Slf4j
@Entity
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    private int account = 0;

    /**
     * 비지니스 팀 요구사항
     * 9. B 상품은 최대 3번까지만 뽑힙니다.
     */
    private int gotB = 0;


    /**
     * 3. 고객은 "가상 지갑"에 돈을 충전할 수 있습니다.
     *     뽑기한 수 만큼 "가상 지갑"에서 돈이 차감됩니다.
     */
    public void charge(){
        this.account += 10000;
        log.info("member.charge() 호출, 충전 후 잔액 ={} ",this.account);
    }


    /**
     * 2. 뽑기 1회당 100원의 돈이 차감됩니다.
     *     즉, 200원을 고객이 사용하면, 뽑기 서비스는 총 2번의 뽑기 기회를 제공합니다.
     */
    public void pay(){
        this.account -= 100;
    }

    public void gotB(){
        this.gotB++;
    }
}
