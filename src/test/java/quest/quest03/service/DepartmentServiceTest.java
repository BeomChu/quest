package quest.quest03.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import quest.quest03.domain.Department;
import quest.quest03.dto.request.DepartmentInfo;
import quest.quest03.dto.request.Relation;
import quest.quest03.dto.response.DepartmentResponse;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DepartmentServiceTest {

    @Autowired
    DepartmentService service;

    @Autowired
    EntityManager em;


    @Test
    @DisplayName("부서 등록 테스트")
    public void departmentRegisterTest(){
        DepartmentInfo info = new DepartmentInfo("QA", 100);
        DepartmentResponse<?> response = service.departmentInfoRegister(info);
        Department data = (Department) response.getData();

        assertEquals(response.getCode(), 200);
        assertEquals(response.getMessage(), "부서 등록 완료");
        assertEquals(data.getName(), info.getDepartment());
        assertEquals(data.getCount(), info.getCount());
    }

    @Test
    @DisplayName("이미 등록된 부서는 등록할 수 없다.")
    public void departmentRegisterBadCase(){
        DepartmentInfo info = new DepartmentInfo("QA", 100);
        DepartmentResponse<?> response = service.departmentInfoRegister(info);

        em.flush();
        em.clear();

        DepartmentResponse<?> response2 = service.departmentInfoRegister(info);
        Department data = (Department) response2.getData();

        assertEquals(response2.getCode(), 400);
        assertEquals(response2.getMessage(), "이미 등록된 부서입니다. 수정하려면 수정서비스를 이용해주세요.");
        assertEquals(data.getName(), info.getDepartment());
        assertEquals(data.getCount(), info.getCount());
    }


    @Test
    @DisplayName("관계 정의 테스트")
    public void relationRegister(){
        DepartmentInfo dmInfo1 = new DepartmentInfo("QA", 100);
        DepartmentInfo dmInfo2 = new DepartmentInfo("DEV", 100);
        service.departmentInfoRegister(dmInfo1);
        service.departmentInfoRegister(dmInfo2);

        Relation info = new Relation("QA" ,"DEV");
        DepartmentResponse<?> response = service.relationRegister(info);
        assertEquals(response.getCode(), 200);
        assertEquals(response.getMessage(), "부서 관계 등록이 완료 되었습니다.");
    }

    @Test
    @DisplayName("등록 안된 부서는 관계 정의가 안된다")
    public void relationRegisterBadCase(){
        DepartmentInfo dmInfo2 = new DepartmentInfo("DEV", 100);
        service.departmentInfoRegister(dmInfo2);

        Relation info = new Relation("QA" ,"DEV");
        DepartmentResponse<?> response = service.relationRegister(info);
        assertEquals(response.getCode(), 400);
        assertEquals(response.getMessage(), "등록되지 않은 부서는 부서관계 등록이 불가능합니다.");
    }


    @Test
    @DisplayName("다중 트리 구조 자식의 모든 카운트를 조회한다.")
    public void getTotalCount(){
        DepartmentInfo dmInfo1 = new DepartmentInfo("QA", 100);
        DepartmentInfo dmInfo2 = new DepartmentInfo("DEV", 100);
        DepartmentInfo dmInfo3 = new DepartmentInfo("C", 100);
        DepartmentInfo dmInfo4 = new DepartmentInfo("D", 100);
        service.departmentInfoRegister(dmInfo1);
        service.departmentInfoRegister(dmInfo2);
        service.departmentInfoRegister(dmInfo3);
        service.departmentInfoRegister(dmInfo4);

        Relation relation1 = new Relation("QA", "DEV");
        Relation relation2 = new Relation("DEV", "C");
        Relation relation3 = new Relation("C", "D");
        service.relationRegister(relation1);
        service.relationRegister(relation2);
        service.relationRegister(relation3);

        DepartmentResponse<?> response = service.searchDepartmentTotalCount(dmInfo1.getDepartment());
        assertEquals(response.getCode(), 200);
        assertEquals(response.getMessage(), dmInfo1.getDepartment());
        assertEquals(response.getData(), 400);
    }




}