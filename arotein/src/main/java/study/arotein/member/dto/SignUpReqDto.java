package study.arotein.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReqDto {
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    @Length(min = 3, max = 60, message = "비밀번호는 3~60자만 가능합니다.")
    private String password;
    @NotBlank
    @Length(min = 3, max = 8, message = "이름은 3~8자만 가능합니다.")
    private String username;
}
