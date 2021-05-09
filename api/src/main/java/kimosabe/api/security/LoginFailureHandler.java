package kimosabe.api.security;

import lombok.NoArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component @NoArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response, AuthenticationException ex
    ) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
