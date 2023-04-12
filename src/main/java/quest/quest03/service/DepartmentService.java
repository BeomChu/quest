package quest.quest03.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quest.quest03.domain.Department;
import quest.quest03.domain.DepartmentRepository;
import quest.quest03.dto.request.DepartmentInfo;
import quest.quest03.dto.request.Relation;
import quest.quest03.dto.response.DepartmentResponse;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentResponse<?> departmentInfoRegister(DepartmentInfo info) {

        try {
            Department findEntity = departmentRepository.findByName(info.getDepartment());
            if (findEntity == null) {
                Department department = new Department(info.getDepartment(), info.getCount());
                departmentRepository.save(department);
                return new DepartmentResponse<>(200, "부서 등록 완료", department);
            } else if (findEntity != null) {
                return new DepartmentResponse<>(400, "이미 등록된 부서입니다. 수정하려면 수정서비스를 이용해주세요.", findEntity);
            } else {
                return new DepartmentResponse<>(400, "TODO 뭔가 잘못됨", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DepartmentResponse<>(400, "TODO 뭔가 잘못됨", null);
        }
    }

    public DepartmentResponse<?> editDepartmentInfo(DepartmentInfo info) {
        try {
            Department findEntity = departmentRepository.findByName(info.getDepartment());
            Department department = findEntity.editInfo(info);
            return new DepartmentResponse<>(200, "수정완료됨", department);
        } catch (Exception e) {
            return new DepartmentResponse<>(400, "TODO 뭔가 잘못됨", e.getMessage());
        }
    }

    public DepartmentResponse<?> relationRegister(Relation relation) {
        log.info("relationRegister start = {}", relation.toString());

        try {
            Department childEntity = departmentRepository.findByName(relation.getChild());
            if (childEntity.getParent() == null) { //부모는 하나만 등록 가능
                Department parentEntity = departmentRepository.findByName(relation.getParent());
                parentEntity.addChildren(childEntity);
                return new DepartmentResponse<>(200, "부서 관계 등록이 완료 되었습니다.", childEntity.getParent());
            } else {
                return new DepartmentResponse<>(400, "상위 부서는 하나만 등록 가능합니다.", childEntity.getParent());
            }
        } catch (NullPointerException e) {
            return new DepartmentResponse<>(400, "등록되지 않은 부서는 부서관계 등록이 불가능합니다.", null);
        } catch (Exception e) {
            return new DepartmentResponse<>(400, "TODO 뭔가 잘못됨", e.getMessage());
        }
    }

    public DepartmentResponse<?> searchDepartmentTotalCount(String superParentName) {
        log.info("totalCount - start");
        List<Department> all = departmentRepository.findAll();
        Department superParent = all.stream()
                .filter(department -> superParentName.equals(department.getName()))
                .findAny()
                .orElseThrow(()-> new IllegalArgumentException("찾을 수 없는 부서입니다."));
//        Department superParent = departmentRepository.findByName(superParentName);
        int totalCount = 0;
        totalCount += superParent.getCount();
        totalCount += getChildCount(superParent);

        log.info("totalCount - end");
        return new DepartmentResponse<>(200, superParent.getName(), totalCount);

    }

    public int getChildCount(Department parent) {
        int result = 0;
        for (int i = 0; i < parent.getChildren().size(); i++) {
            Department child = parent.getChildren().get(i);
            result += child.getCount();
            result += getChildCount(child);
        }
        return result;
    }


}
