package study.arotein.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import study.arotein.security.filter.JsonLoginProcessingFilter;
import study.arotein.security.handler.JsonAuthenticationFailureHandler;
import study.arotein.security.handler.JsonAuthenticationSuccessHandler;

public class JsonLoginConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractAuthenticationFilterConfigurer<H, JsonLoginConfigurer<H>, JsonLoginProcessingFilter> {

    private AuthenticationManager authenticationManager;
    private JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler;
    private JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler;
    private ObjectMapper objectMapper;

    public JsonLoginConfigurer(String loginProcessingUrl) {
        super(new JsonLoginProcessingFilter(loginProcessingUrl), loginProcessingUrl);
    }

    @Override
    public void init(H http) throws Exception {
        super.init(http);
    }

    @Override
    public void configure(H http) throws Exception {
        JsonLoginProcessingFilter jsonFilter = getAuthenticationFilter();
        jsonFilter.setAuthenticationManager(authenticationManager);
        jsonFilter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler);
        jsonFilter.setAuthenticationFailureHandler(jsonAuthenticationFailureHandler);
        jsonFilter.setObjectMapper(objectMapper);

        SessionAuthenticationStrategy sessionStrategy = http.getSharedObject(SessionAuthenticationStrategy.class);
        if (sessionStrategy != null) {
            jsonFilter.setSessionAuthenticationStrategy(sessionStrategy);
        }
        RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);
        if (rememberMeServices != null) {
            jsonFilter.setRememberMeServices(rememberMeServices);
        }
        http.setSharedObject(JsonLoginProcessingFilter.class, jsonFilter);
        http.addFilterBefore(jsonFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    public JsonLoginConfigurer<H> loginSuccessHandler(JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler) {
        this.jsonAuthenticationSuccessHandler = jsonAuthenticationSuccessHandler;
        return this;
    }

    public JsonLoginConfigurer<H> loginFailureHandler(JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler) {
        this.jsonAuthenticationFailureHandler = jsonAuthenticationFailureHandler;
        return this;
    }

    public JsonLoginConfigurer<H> objectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public JsonLoginConfigurer<H> addAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        return this;
    }
}
