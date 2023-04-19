package quest.quest01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quest.ex.customEx.CustomInvalidException;
import quest.ex.customEx.CustomNotFoundException;
import quest.quest01.domain.changer.Changer;
import quest.quest01.domain.changer.ChangerRepository;
import quest.quest01.domain.code.Code;
import quest.quest01.domain.code.CodeRepository;
import quest.quest01.dto.response.ResponseDto;
import quest.quest01.util.CodeFactory;
import quest.quest01.util.HelpMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static quest.quest01.type.Response.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;
    private final ChangerRepository changerRepository;


    public void createCodeList() {
        log.info("CreateCodeList-Start");
        List<Code> codeList = new ArrayList<>();
        while (codeList.size() < 10) {
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

    public ResponseDto<?> offerCodeList(Long changerId) {
        log.info("getCodeList-start");
        createCodeList();
        Changer changer = changerRepository.findById(changerId)
                .orElseThrow(() -> new CustomNotFoundException(NOT_FOUND_CHANGER));

        log.info("offerCodeList to {}", changer.getName());

        PageRequest pageRequest = PageRequest.of(CodeFactory.page++, 10);

        Page<Code> findAll = codeRepository.findAll(pageRequest);
        log.info("findAll.content{}",findAll);

        log.info("findAll.size = {}", findAll.getTotalElements());


        changer.offerCodeList(findAll.getContent());
        changerRepository.save(changer);

        log.info("getCodeList-end");
        return new ResponseDto<>(SUC_OFFER, findAll.getContent());
    }

    public ResponseDto<?> checkItemCode(String itemCode) throws Exception {
        log.info("checkItemCode-Start itemCode = [{}]", itemCode);

        if (itemCode.length() != 9 || !Pattern.matches("^[0-9]*$", itemCode)) {
            throw new CustomInvalidException(BAD_REQUEST_ITEM);
        }

        Code findCode = codeRepository.findByItemCode(itemCode);
        if (findCode == null) {
            throw new CustomNotFoundException(NOT_FOUND_ITEM);
        }

        if (findCode.isChanged()) {
            return new ResponseDto<>(ALREADY_CHANGED, ALREADY_CHANGED.sayStoreCode(findCode.getStoreCode()));
        } else {
            return new ResponseDto<>(YET_CHANGED, findCode); // codeEntity 반환
        }


    }


    public ResponseDto<?> claimItemCode(String storeCode, String itemCode) throws Exception {
        log.info("ClaimItemCode-Start storeCode = [{}] itemCode = [{}]", storeCode, itemCode);

        if (storeCode.length() != 6 || !Pattern.matches("^[a-zA-Z]*$", storeCode)) {
            throw new CustomInvalidException(BAD_REQUEST_STORE);
        }

        ResponseDto<?> responseDto = checkItemCode(itemCode);
        if (!responseDto.getMessage().equals(YET_CHANGED.getMessage())) {
            log.info("ClaimItemCode-End {}", YET_CHANGED.getMessage());
            return responseDto;
        } else {
            Code code = (Code) responseDto.getData();
            code.changeItem(storeCode);

            log.info("ClaimItemCode-End {}", SUC_CLAIM.getMessage());
            return new ResponseDto<>(SUC_CLAIM, "교환된 상점 코드 : " + storeCode);
        }
    }

    public ResponseDto<?> helpMessage() {
        return new ResponseDto<>(HELP_MESSAGE, HelpMessage.helpMessage());
    }


}
