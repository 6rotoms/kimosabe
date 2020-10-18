package kimosabe.api.controller;

import kimosabe.api.constants.AppConstants;
import kimosabe.api.entity.LoginDetailsRequestBody;
import kimosabe.api.model.User;
import kimosabe.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("auth")
public class AuthController {
    private AuthenticationManager authManager;
    private UserService userService;
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @Autowired
    public AuthController(
            AuthenticationManager authManager,
            UserService userService,
            FindByIndexNameSessionRepository<? extends Session> sessionRepository
    ) {
        this.authManager = authManager;
        this.userService = userService;
        this.sessionRepository = sessionRepository;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(
            @RequestBody LoginDetailsRequestBody loginDetails
    ) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDetails.getUsername(), loginDetails.getPassword()));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void register(
            @RequestBody LoginDetailsRequestBody loginDetails
    ) {
        String username = loginDetails.getUsername();
        String password = loginDetails.getPassword();
        userService.createNewUser(new User(username, password));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
