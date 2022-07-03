package study.arotein.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class SecurityLoginReqDto {
    @Email
    @NotNull
    private String email;
    @NotEmpty
    private String password;
}
