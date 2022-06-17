package study.arotein.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.arotein.exception.CustomException;

/**
 * success : 처리 성공이면 true, 실패면 false
 */

@Setter
@Getter
@NoArgsConstructor
public class ResponseBase {
    private Boolean success = true;
    private String redirect;
    private Error error;
    private Object data;

    public ResponseBase(Exception ex) {
        this.success = false;
        this.error = new Error(0, ex.getMessage());
    }

    public ResponseBase(CustomException ex) {
        this.success = false;
        this.redirect = ex.getRedirect();
        this.error = new Error(ex.getErrorCode(), ex.getMessage());
        this.data = ex.getData();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Error {
        private Integer errorCode;
        private String message;
    }
}
