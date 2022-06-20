package study.arotein.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;

@Getter
@Setter
public class CustomAuthenticationException extends AuthenticationException {

    private Integer errorCode;
    private Integer httpStatus;
    private String redirect;
    private Object data;

    public CustomAuthenticationException(Integer errorCode, Integer httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public CustomAuthenticationException(Integer errorCode, Integer httpStatus, String message, String redirect) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.redirect = redirect;
    }

    public CustomAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CustomAuthenticationException(String msg) {
        super(msg);
    }
}
