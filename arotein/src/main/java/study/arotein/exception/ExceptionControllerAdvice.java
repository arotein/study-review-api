package study.arotein.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import study.arotein.base.ResponseBase;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler
    public ResponseBase runtimeExHandle(RuntimeException ex) {
        log.error("Exception Name = {}, Message = {}", ex.getClass().getName(), ex.getMessage());
        return new ResponseBase(ex);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseBase customExHandle(CustomException ex) {
        log.error("Exception Name = {}, Message = {}, ErrorCode = {}", ex.getClass().getName(), ex.getMessage(), ex.getErrorCode());
        return new ResponseBase(ex);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseBase dataIntegrityExHandle(DataIntegrityViolationException ex) {
        log.error("Exception Name = {}, Message = {}", ex.getClass().getName(), ex.getMessage());
        ResponseBase responseBase = new ResponseBase(ex);
        responseBase.getError().setMessage("데이터 무결성에 위반됩니다.");
        return responseBase;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseBase accessDeniedExHandler(AccessDeniedException ex) {
        log.error("Exception Name = {}, Message = {}", ex.getClass().getName(), ex.getMessage());
        return new ResponseBase(ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseBase constraintViolationExHandler(ConstraintViolationException ex) {
        log.error("Exception Name = {}, Message = {}", ex.getClass().getName(), ex.getMessage());
        return new ResponseBase(ex);
    }
}
