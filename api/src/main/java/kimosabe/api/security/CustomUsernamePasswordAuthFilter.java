package kimosabe.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kimosabe.api.entity.LoginDetailsRequestBody;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Setter
public class CustomUsernamePasswordAuthFilter extends AbstractAuthenticationProcessingFilter {
    private static final AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/auth/login",
            "POST");
    private ObjectMapper objectMapper;

    @Autowired
    public CustomUsernamePasswordAuthFilter(
            ObjectMapper objectMapper
            ) {
        super(requestMatcher);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication (
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException, IOException, ServletException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        LoginDetailsRequestBody loginDetails = getLoginDetails(request);
        String username = (loginDetails.getUsername() != null) ? loginDetails.getUsername() : "";
        username = username.trim();
        String password = (loginDetails.getPassword() != null) ? loginDetails.getPassword() : "";
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        // Allow subclasses to set the "details" property
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private LoginDetailsRequestBody getLoginDetails(HttpServletRequest request) {
        LoginDetailsRequestBody loginRequest =
                (LoginDetailsRequestBody) request.getAttribute(LoginDetailsRequestBody.class.getName());
        if(loginRequest == null){
            try{
                loginRequest = this.objectMapper.readValue(request.getReader(), LoginDetailsRequestBody.class);
                request.setAttribute(LoginDetailsRequestBody.class.getName(), loginRequest);
            }catch (IOException ex){
                loginRequest = new LoginDetailsRequestBody();
            }
        }
        return loginRequest;
    }


}
