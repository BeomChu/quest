package quest.quest03.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import quest.ex.customEx.CustomInvalidException;
import quest.quest01.type.Response;
import quest.quest03.dto.request.DepartmentInfo;
import quest.quest03.dto.request.Relation;
import quest.quest03.service.DepartmentService;

import static quest.quest01.type.Response.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/department")
    public String index(@RequestParam String request){
        log.info("DepartmentController-start request={}", request);
            if (request.contains(",")) {
                String[] info = request.replaceAll("\\s", "").split(",");
                return departmentService.departmentInfoRegister(new DepartmentInfo(info[0], Integer.parseInt(info[1])));
            } else if (request.contains(">")) {
                String[] relation = request.replaceAll("\\s", "").split(">");
                return departmentService.relationRegister(new Relation(relation[0], relation[1]));
            } else {
                throw new CustomInvalidException(INVALID);
            }
    }

}
