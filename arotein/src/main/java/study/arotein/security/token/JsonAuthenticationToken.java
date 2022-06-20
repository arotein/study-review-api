package study.arotein.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import study.arotein.security.auth.UserDetailsImpl;
import study.arotein.security.dto.SecurityLoginReqDto;

public class JsonAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public JsonAuthenticationToken(SecurityLoginReqDto loginReqDto) {
        super(loginReqDto.getEmail(), loginReqDto.getPassword());
    }

    public JsonAuthenticationToken(UserDetailsImpl userDetails) {
        super(userDetails, null, userDetails.getAuthorities());
    }
}
