package quest.quest01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import quest.quest01.domain.Code;
import quest.quest01.domain.CodeRepository;
import quest.quest01.domain.type.Request;
import quest.quest01.domain.type.Response;
import quest.quest01.domain.type.ResponseMessage;
import quest.quest01.dto.response.ResponseDto;
import quest.quest01.util.CodeFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;

    /**
     * 버그로 인해 Code생성자에서 사용하던 codeFactory를 while문으로 이동
     * 함수명 codeFactory -> createCodeList로 변경
     */
    public void createCodeList() {
        log.info("CreateCodeList-Start");
        List<Code> codeList = new ArrayList<>();
        while (codeList.size() < 20) {
            Code code = new Code(CodeFactory.itemCodeFactory());
            if (codeRepository.existsByItemCode(code.getItemCode())) {
                continue;
            } else {
                codeList.add(code);
            }
        }

        codeRepository.saveAll(codeList);
        log.info("CreateCodeList-End");
    }

    public Page<Code> getCodeList() {
        int page = 0;
        PageRequest pageRequest = PageRequest.of(page++, 10);
        Page<Code> findAll = codeRepository.findAll(pageRequest);
        if(findAll.getTotalPages() == page) {
            createCodeList();
        }
        return findAll;
    }

    public ResponseDto<?> checkItemCode(String request) {
        log.info("checkItemCode-Start request = [{}]", request);

        try {
            String itemCode = request.replaceAll("\\s", "")
                    .toLowerCase().substring(5, 14);
            log.info("replacedItemCode = [{}]", itemCode);
            Code findCode = codeRepository.findByItemCode(itemCode);
            if (findCode == null) {
                return new ResponseDto<>(
                        ResponseMessage.NotFoundItem.code(),
                        ResponseMessage.NotFoundItem.message(),
                        null);
            }

            log.info("inchanged [{}][{}]", findCode.isChanged(), findCode.getStoreCode());

            if (findCode.isChanged()) {
                return new ResponseDto<>(
                        ResponseMessage.AlreadyChanged.code(),
                        ResponseMessage.AlreadyChanged.message(),
                        ResponseMessage.AlreadyChanged.sayStoreCode(findCode.getStoreCode()));
            } else {
                return new ResponseDto<>(
                        ResponseMessage.FindItemCode.code(),
                        ResponseMessage.FindItemCode.message(),
                        findCode); // codeEntity 반환
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDto<>(
                    ResponseMessage.BadRequest.code(),
                    ResponseMessage.BadRequest.message(),
                    null);
        } finally {
            log.info("checkItemCode-End");
        }

    }

    public ResponseDto<?>  claimItemCode(String request) {
        log.info("ClaimItemCode-Start request = [{}]", request);

        try {
            request.replaceAll("\\s", "");
            String storeCode = request.substring(5, 11);
            String itemCode = request.substring(11, 20);
            ResponseDto<?> responseDto = checkItemCode(Request.CHECK + itemCode);
            if (responseDto.getCode() != 200) {
                return responseDto; //responseDto 그대로 반환
            } else {
                Code findCode = (Code) responseDto.getData();
                findCode.changeItem(storeCode);

                return new ResponseDto<>(ResponseMessage.SucClaim.code(),
                        ResponseMessage.SucClaim.message(),
                        ResponseMessage.SucClaim.sayStoreCode(storeCode));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDto<>(ResponseMessage.BadRequest.code(),
                    ResponseMessage.BadRequest.message(),
                    null);
        } finally {
            log.info("ClaimItemCode-End");

        }


    }

    public ResponseDto<?> helpMessage() {
        return new ResponseDto<>(
                ResponseMessage.HelpMessage.code(),
                ResponseMessage.HelpMessage.message(),
                ResponseMessage.HelpMessage.helpMessage());
    }


}
