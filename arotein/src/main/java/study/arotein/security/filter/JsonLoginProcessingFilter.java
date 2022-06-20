package study.arotein.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import study.arotein.exception.CustomAuthenticationException;
import study.arotein.security.dto.SecurityLoginReqDto;
import study.arotein.security.token.JsonAuthenticationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * 에러코드 : 1000~1100
 */
public class JsonLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private ObjectMapper objectMapper;

    public JsonLoginProcessingFilter(String processUrl) {
        super(new AntPathRequestMatcher(processUrl, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // request type 검증
        if (!isJson(request) || !request.getMethod().equals("POST")) {
            throw new CustomAuthenticationException(1000, HttpStatus.METHOD_NOT_ALLOWED.value(), "JSON 또는 POST요청이 아닙니다.");
        }
        // request parameter 검증
        SecurityLoginReqDto loginReqDto = objectMapper.readValue(request.getReader(), SecurityLoginReqDto.class);
        if (!StringUtils.hasText(loginReqDto.getEmail()) || !StringUtils.hasText(loginReqDto.getPassword())) {
            throw new CustomAuthenticationException(1001, HttpStatus.BAD_REQUEST.value(), "email 또는 password가 공백입니다.");
        }
        // 임시token 생성
        JsonAuthenticationToken token = new JsonAuthenticationToken(loginReqDto);

        return getAuthenticationManager().authenticate(token);
    }

    private boolean isJson(HttpServletRequest request) {
        String headerType = request.getHeader("Content-Type");
        if (headerType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            return true;
        }
        return false;
    }

    public void setObjectMapper(ObjectMapper mapper) {
        this.objectMapper = mapper;
    }
}
