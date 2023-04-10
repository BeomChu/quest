package quest.quest01.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import quest.quest01.domain.type.Request;
import quest.quest01.dto.request.RequestDto;
import quest.quest01.dto.response.ResponseDto;
import quest.quest01.service.CodeService;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CodeController {

    public final CodeService codeService;

    @GetMapping("/index")
    public ResponseDto<?> index(@RequestBody RequestDto request){
        String requestType = "";
        try {
            requestType = request.getRequest().substring(0,5).toLowerCase();
            System.out.println("requestType =========" + requestType);
        } catch (Exception e) {
            return codeService.helpMessage();
        }
        switch (requestType) {
            case "check" :
                return codeService.checkItemCode(requestType);
            case "claim" :
                return codeService.claimItemCode(requestType);
            default :
                return codeService.helpMessage();
        }
    }
}
