package study.arotein.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import study.arotein.base.ResponseBase;
import study.arotein.exception.CustomAuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        CustomAuthenticationException authException = (CustomAuthenticationException) exception;
        ResponseBase responseBase = new ResponseBase(authException);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(authException.getHttpStatus());
        objectMapper.writeValue(response.getWriter(), responseBase);
    }
}
