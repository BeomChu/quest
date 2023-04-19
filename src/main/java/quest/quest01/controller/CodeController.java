package quest.quest01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import quest.ex.customEx.CustomInvalidException;
import quest.quest01.dto.response.ResponseDto;
import quest.quest01.service.CodeService;

import static quest.quest01.type.Response.*;


@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CodeController {

    public final CodeService codeService;

    @GetMapping("/index")
    public ResponseDto<?> index(@RequestParam String request) throws Exception {
        String replaceRequest = request.replaceAll("\\s", "");
        log.info("index request = {}", request);
        try {
            if (replaceRequest.toLowerCase().startsWith("check")) {
                return codeService.checkItemCode(replaceRequest.substring(5,14));
            } else if (replaceRequest.toLowerCase().startsWith("claim")) {
                String storeCode = replaceRequest.substring(5,11);
                String itemCode = replaceRequest.substring(11,20);

                return codeService.claimItemCode(storeCode, itemCode);
            } else {
                return codeService.helpMessage();
            }
        }catch (StringIndexOutOfBoundsException e) {
            throw new CustomInvalidException(INVALID);
        }

    }
}

