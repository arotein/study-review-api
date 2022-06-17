package study.arotein.member.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpReqDto {
    private String email;
    private String password;
    private String username;
}
