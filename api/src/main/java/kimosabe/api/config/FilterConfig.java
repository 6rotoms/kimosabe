package kimosabe.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kimosabe.api.constants.AppConstants;
import kimosabe.api.security.CustomConcurrentSessionStrategy;
import kimosabe.api.security.CustomUsernamePasswordAuthFilter;
import kimosabe.api.security.LoginFailureHandler;
import kimosabe.api.security.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;

@Configuration
public class FilterConfig {
    private final LoginFailureHandler loginFailureHandler;
    private  final LoginSuccessHandler loginSuccessHandler;
    private final ObjectMapper objectMapper;
    private final CustomConcurrentSessionStrategy sessionStrategy;

    @Autowired
    public FilterConfig (
            LoginFailureHandler loginFailureHandler,
            LoginSuccessHandler loginSuccessHandler,
            ObjectMapper objectMapper,
            CustomConcurrentSessionStrategy sessionStrategy
    ) {
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
        this.objectMapper = objectMapper;
        this.sessionStrategy = sessionStrategy;
    }

    protected CustomUsernamePasswordAuthFilter usernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        CustomUsernamePasswordAuthFilter filter = new CustomUsernamePasswordAuthFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setSessionAuthenticationStrategy(authStrategy());
        return filter;
    }

    private ConcurrentSessionControlAuthenticationStrategy authStrategy() {
        sessionStrategy.setMaximumSessions(AppConstants.MAX_NUM_SESSIONS);
        return sessionStrategy;
    }

}

