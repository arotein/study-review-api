package study.arotein.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 예외처리와 함께 데이터를 같이 응답해야할 때 사용.
 * errorCode : 에러코드
 * redirect : 리다이렉트시킬 url
 * data : view에 전달할 객체
 */

@Setter
@Getter
public class CustomException extends RuntimeException {
    private Integer errorCode;
    private String redirect;
    private Object data;

    public CustomException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(Integer errorCode, String message, String redirect) {
        super(message);
        this.errorCode = errorCode;
        this.redirect = redirect;
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
