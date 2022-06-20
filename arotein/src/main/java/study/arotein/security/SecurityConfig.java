package study.arotein.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import study.arotein.enumeration.Role;
import study.arotein.security.handler.JsonAuthenticationFailureHandler;
import study.arotein.security.handler.JsonAuthenticationSuccessHandler;
import study.arotein.security.handler.JsonLogoutSuccessHandler;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final String JSON_LOGIN_PROCESSING_URL = "/api/login";
    private final String JSON_LOGOUT_PROCESSING_URL = "/api/logout";

    private final AuthenticationProvider authenticationProvider;
    private final JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler;
    private final JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler;
    private final JsonLogoutSuccessHandler jsonLogoutSuccessHandler;
    private final ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http.formLogin().disable();
        http.csrf().disable();

        http.authorizeRequests()
                .anyRequest().permitAll();

        http.logout()
                .logoutUrl(JSON_LOGOUT_PROCESSING_URL)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .logoutSuccessHandler(jsonLogoutSuccessHandler);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(2)
                .maxSessionsPreventsLogin(false);

        addJsonConfigurer(http);
    }

    private void addJsonConfigurer(HttpSecurity http) throws Exception {
        http.apply(new JsonLoginConfigurer<>(JSON_LOGIN_PROCESSING_URL))
                .addAuthenticationManager(authenticationManager())
                .loginSuccessHandler(jsonAuthenticationSuccessHandler)
                .loginFailureHandler(jsonAuthenticationFailureHandler)
                .objectMapper(objectMapper);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        List<AuthenticationProvider> authProviderList = new ArrayList<>();
        authProviderList.add(authenticationProvider);
        ProviderManager providerManager = new ProviderManager(authProviderList);
        return providerManager;
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(String.format("%s > %s > %s", Role.ROLE_ADMIN, Role.ROLE_MANAGER, Role.ROLE_NORMAL));
        return roleHierarchy;
    }

    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }
}
