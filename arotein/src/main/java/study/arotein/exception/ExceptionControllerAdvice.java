package study.arotein.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import study.arotein.base.ResponseBase;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler
    public ResponseBase runtimeExHandle(RuntimeException ex) {
        log.warn("RuntimeException = {}, Message = {}", ex.getStackTrace(), ex.getMessage());
        return new ResponseBase(ex);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseBase customExHandle(CustomException ex) {
        log.warn("CustomException = {}", ex.getStackTrace());
        log.warn("ErrorCode = {}, Message = {}", ex.getErrorCode(), ex.getMessage());
        return new ResponseBase(ex);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseBase dataIntegrityExHandle(DataIntegrityViolationException ex) {
        log.warn("RuntimeException = {}, Message = {}", ex.getStackTrace(), ex.getMessage());
        ResponseBase responseBase = new ResponseBase(ex);
        responseBase.getError().setMessage("데이터 무결성에 위반됩니다.");
        return responseBase;
    }
}
