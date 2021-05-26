package kimosabe.api.controller;

import kimosabe.api.entity.RegistrationDetailsRequestBody;
import kimosabe.api.model.User;
import kimosabe.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@SuppressWarnings("unused")
@RequestMapping("auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void register(
            @Valid @RequestBody RegistrationDetailsRequestBody registrationDetails
    ) {
        String username = registrationDetails.getUsername();
        String email = registrationDetails.getEmail();
        String password = registrationDetails.getPassword();
        userService.createNewUser(new User(username, email, password));
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verify(@RequestParam(name = "token", defaultValue = "") String token) {
        userService.verifyUser(token);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
