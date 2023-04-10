package quest.quest01.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<Code, Long> {

    boolean existsByItemCode(String itemCode);

    Code findByItemCode(String itemCode);
}
