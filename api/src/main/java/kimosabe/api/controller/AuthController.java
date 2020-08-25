package kimosabe.api.controller;

import kimosabe.api.exceptions.UsernameTakenException;
import kimosabe.api.model.User;
import kimosabe.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("auth")
public class AuthController {
    private AuthenticationManager authManager;
    private UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authManager, UserService userService) {
        this.authManager = authManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestHeader String username,
            @RequestHeader String password,
            HttpSession session
    ) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestHeader String username,
            @RequestHeader String password
    ) {
        try {
            userService.createNewUser(new User(username, password));
        } catch (UsernameTakenException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
