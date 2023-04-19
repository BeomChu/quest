package quest.quest03.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quest.ex.customEx.CustomDepartmentException;
import quest.ex.customEx.CustomInvalidException;
import quest.ex.customEx.CustomNotFoundException;
import quest.quest01.type.Response;
import quest.quest03.domain.Department;
import quest.quest03.domain.DepartmentRepository;
import quest.quest03.dto.request.DepartmentInfo;
import quest.quest03.dto.request.Relation;

import java.util.List;
import java.util.regex.Pattern;

import static quest.quest01.type.Response.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public String departmentInfoRegister(DepartmentInfo info) {
        log.info("departmentInfoRegister start = {}", info.toString());

        if (!Pattern.matches("^[A-Z]*$", info.getDepartment())) {
            throw new CustomDepartmentException(INVALID);
        }

        Department findEntity = departmentRepository.findByName(info.getDepartment());
        if (findEntity == null) {
            Department department = new Department(info.getDepartment(), info.getCount());
            departmentRepository.save(department);
            return info.getDepartment() +", "+info.getCount();
        } else {
            throw new CustomDepartmentException(ALREADY_REGISTERED);
        }
    }

    public String relationRegister(Relation relation) {
        log.info("relationRegister start = {}", relation.toString());
        if (relation.getParent().equals("*")) {
            Department childEntity = departmentRepository.findByName(relation.getChild());
            if (childEntity == null) {
                throw new CustomDepartmentException(NOT_FOUND_DEPARTMENT);
            } else {
                childEntity.removeParent(childEntity.getParent());
                return getResult(childEntity);
            }
        }

        if (!Pattern.matches("^[A-Z]*$", relation.getParent()) ||
                !Pattern.matches("^[A-Z]*$", relation.getChild())) {
            throw new CustomDepartmentException(INVALID);
        }

        try {
            Department childEntity = departmentRepository.findByName(relation.getChild());
            if (childEntity.getParent() == null) {
                Department parentEntity = departmentRepository.findByName(relation.getParent());
                parentEntity.addChildren(childEntity);
                return getResult(childEntity);
            } else {
                throw new CustomDepartmentException(PARENT_ONLY_ONE);
            }
        } catch (NullPointerException e) {
            throw new CustomDepartmentException(NOT_FOUND_DEPARTMENT);
        }
    }

    private String getResult(Department childEntity) {
        List<Department> all = departmentRepository.findAll();
        Department parent = childEntity.getParent();
        if (childEntity.getParent() == null) {
            return childEntity.getName() + ", "+ getChildCount(childEntity);
        }

        while (parent.getParent() != null) {
            parent = parent.getParent();
        }
        return parent.getName()+ ", "+getChildCount(parent);

    }

    private int getChildCount(Department parent) {
        int result = 0;
        result += parent.getCount();
        for (int i = 0; i < parent.getChildren().size(); i++) {
            Department child = parent.getChildren().get(i);
            result += getChildCount(child);
        }
        return result;
    }


}
