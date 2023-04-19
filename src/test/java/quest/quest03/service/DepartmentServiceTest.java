package quest.quest03.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import quest.ex.customEx.CustomDepartmentException;
import quest.ex.customEx.CustomInvalidException;
import quest.ex.customEx.CustomNotFoundException;
import quest.quest01.type.Response;
import quest.quest03.domain.Department;
import quest.quest03.domain.DepartmentRepository;
import quest.quest03.dto.request.DepartmentInfo;
import quest.quest03.dto.request.Relation;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static quest.quest01.type.Response.*;


@SpringBootTest
@Transactional
class DepartmentServiceTest {

    @Autowired
    DepartmentService service;

    @Autowired
    DepartmentRepository repository;

    @Autowired
    EntityManager em;


    @Test
    @DisplayName("부서등록 테스트")
    public void infoTest01(){
        DepartmentInfo info = new DepartmentInfo("QA",100);
        String result = service.departmentInfoRegister(info);

        Department qa = repository.findByName("QA");

        assertEquals(result, "QA, 100");
        assertEquals(qa.getName(), "QA");
        assertEquals(qa.getCount(), 100);
    }

    @Test
    @DisplayName("부서등록 중복 테스트")
    public void infoTest02(){
        DepartmentInfo info = new DepartmentInfo("QA",100);
        String result = service.departmentInfoRegister(info);

        CustomDepartmentException ex =
                assertThrows(CustomDepartmentException.class, () -> service.departmentInfoRegister(info));

        assertEquals(ex.getMessage(), ALREADY_REGISTERED.getMessage());
    }

    @Test
    @DisplayName("부서 이름은 대문자여야한다.")
    public void infoTest03(){
        DepartmentInfo info = new DepartmentInfo("qa",100);

        CustomDepartmentException ex =
                assertThrows(CustomDepartmentException.class, () -> service.departmentInfoRegister(info));

        assertEquals(ex.getMessage(), INVALID.getMessage());
    }


    @Test
    @DisplayName("등록안된 부서는 부서관계등록이 되지 않는다")
    public void relationTest01(){
        Relation relation = new Relation("QA", "DEV");

        CustomDepartmentException ex =
                assertThrows(CustomDepartmentException.class, () -> service.relationRegister(relation));

        assertEquals(ex.getMessage(), NOT_FOUND_DEPARTMENT.getMessage());
    }

    @Test
    @DisplayName("등록안된 부서는 등록이 되지 않는다 YetRegisteredParent")
    public void relationTest02(){
        DepartmentInfo info = new DepartmentInfo("DEV",100);
        String result = service.departmentInfoRegister(info);

        Relation relation = new Relation("QA", "DEV");

        CustomDepartmentException ex =
                assertThrows(CustomDepartmentException.class, () -> service.relationRegister(relation));

        assertEquals(ex.getMessage(), NOT_FOUND_DEPARTMENT.getMessage());
    }


    @Test
    @DisplayName("등록안된 부서는 등록이 되지 않는다 YetRegisteredChild")
    public void relationTest03(){
        DepartmentInfo info = new DepartmentInfo("QA",100);
        String result = service.departmentInfoRegister(info);

        Relation relation = new Relation("QA", "DEV");

        CustomDepartmentException ex =
                assertThrows(CustomDepartmentException.class, () -> service.relationRegister(relation));

        assertEquals(ex.getMessage(), NOT_FOUND_DEPARTMENT.getMessage());
    }


    @Test
    @DisplayName("부서구성도는 대문자로 입력된다.")
    public void relationTest04(){
        Department qa = new Department("QA", 100);
        Department dev = new Department("DEV", 100);

        repository.save(qa);
        repository.save(dev);

        Relation relation = new Relation("qa", "dev");

        CustomDepartmentException ex =
                assertThrows(CustomDepartmentException.class, () -> service.relationRegister(relation));

        assertEquals(ex.getMessage(), INVALID.getMessage());
    }

    @Test
    @DisplayName("등록된 부서들의 부서구성도 입력시 상위 부서의 총 인원을 출력한다")
    public void relationTest05(){
        Department qa = new Department("QA", 100);
        Department dev = new Department("DEV", 100);
        Department as = new Department("AS", 100);
        Department a = new Department("A", 100);
        Department b = new Department("B", 100);
        Department c = new Department("C", 100);
        Department d = new Department("D", 100);

        repository.save(qa);
        repository.save(dev);
        repository.save(as);
        repository.save(a);
        repository.save(b);
        repository.save(c);
        repository.save(d);

        Relation relation1 = new Relation("QA", "DEV");
        Relation relation2 = new Relation("DEV", "A");
        Relation relation3 = new Relation("DEV", "B");

        Relation relation4 = new Relation("AS", "C");
        Relation relation5 = new Relation("AS", "D");

        String result1 = service.relationRegister(relation1);
        assertEquals(result1, "QA, 200");

        String result2 = service.relationRegister(relation2);
        assertEquals(result2, "QA, 300");

        String result3 = service.relationRegister(relation3);
        assertEquals(result3, "QA, 400");

        String result4 = service.relationRegister(relation4);
        assertEquals(result4, "AS, 200");

        String result5 = service.relationRegister(relation5);
        assertEquals(result5, "AS, 300");
    }

    @Test
    @DisplayName("상위부서가 있더라도 최상위부서로 입력된 부서는 최상위 부서가된다.")
    public void relationTest06(){
        Department qa = new Department("QA", 100);
        Department dev = new Department("DEV", 100);
        Department a = new Department("A", 100);
        Department b = new Department("B", 100);

        repository.save(qa);
        repository.save(dev);
        repository.save(a);
        repository.save(b);

        Relation relation1 = new Relation("QA", "DEV");
        Relation relation2 = new Relation("DEV", "A");
        Relation relation3 = new Relation("DEV", "B");

        Relation relation4 = new Relation("*", "B");



        service.relationRegister(relation1);
        service.relationRegister(relation2);
        String before = service.relationRegister(relation3);
        String after = service.relationRegister(relation4);

        assertEquals(before, "QA, 400");
        assertEquals(after, "B, 100");
    }

}