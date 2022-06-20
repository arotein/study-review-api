package study.arotein.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SecurityLoginReqDto {
    private String email;
    private String password;
}
