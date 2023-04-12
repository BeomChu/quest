package quest.quest03.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import quest.quest03.dto.request.DepartmentInfo;

import javax.persistence.NamedQuery;
import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByName(String name);
    boolean existsByName(String name);

    @Query(value = "SELECT d FROM department d WHERE d.name = :parent and d.name = :child", nativeQuery = true)
    List<Department> findByInfo(String parent, String child);
}
