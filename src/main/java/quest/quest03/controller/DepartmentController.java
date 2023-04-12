package quest.quest03.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import quest.quest03.dto.request.DepartmentInfo;
import quest.quest03.dto.request.Relation;
import quest.quest03.dto.request.RequestInfo;
import quest.quest03.dto.response.DepartmentResponse;
import quest.quest03.service.DepartmentService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3")
public class DepartmentController {

    private final DepartmentService service;

    @GetMapping("/info")
    public DepartmentResponse<?> getDepartmentInfo(@RequestBody RequestInfo info) {
        return service.searchDepartmentTotalCount(info.getDepartmentName());
    }

    @PostMapping("/info")
    public DepartmentResponse<?> departmentInfoRegister(@RequestBody DepartmentInfo departmentInfo){
        return service.departmentInfoRegister(departmentInfo);
    }

    @PostMapping("/relation")
    public DepartmentResponse relationResgister(@RequestParam Relation relation){
        log.info("relationRegister relation={}" ,relation.toString());
        return service.relationRegister(relation);
    }
}
