package quest.ex.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import quest.ex.customEx.CustomDepartmentException;
import quest.ex.customEx.CustomInvalidException;
import quest.ex.customEx.CustomNotFoundException;
import quest.quest01.dto.response.ResponseDto;

import static quest.quest01.util.HelpMessage.*;


@Slf4j
@RestControllerAdvice
public class CustomExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomInvalidException.class)
    public ResponseDto<?> customInvalidException(CustomInvalidException e){
        return new ResponseDto<>(e.getResponse(), helpMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseDto<?> customNotFoundException(CustomNotFoundException e){
        return new ResponseDto<>(e.getResponse(), helpMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public String numberFormatHandle(NumberFormatException e) {
        return "인원수는 숫자로 입력해주세요";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomDepartmentException.class)
    public String forDepartmentHandle(CustomDepartmentException e) {
        return e.getMessage();
    }


}
