package kimosabe.api.controller;

import kimosabe.api.model.User;
import kimosabe.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> changePassword(@RequestParam("newPassword") String newPassword, @RequestParam("oldPassword") String oldPassword) {
        User user = userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (userService.checkPassword(user, oldPassword)){
            userService.changePassword(user, newPassword);
            return new ResponseEntity<String>("Password changed successfully!", HttpStatus.OK);
        }else{
            return new ResponseEntity<String>("Incorrect old password!",HttpStatus.BAD_REQUEST);
        }
    }
}