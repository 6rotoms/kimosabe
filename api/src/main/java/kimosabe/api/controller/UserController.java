package kimosabe.api.controller;

import kimosabe.api.entity.*;
import kimosabe.api.exception.IncorrectPasswordException;
import kimosabe.api.model.User;
import kimosabe.api.service.GroupService;
import kimosabe.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private GroupService groupService;

    @Autowired
    public UserController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody ChangePasswordRequestBody changePassword) {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userService.checkPassword(user, changePassword.getOldPassword())) {
            userService.changePassword(user, changePassword.getNewPassword());
        } else {
            throw new IncorrectPasswordException();
        }
    }

    @PostMapping("/friends")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public void requestFriend(@RequestBody FriendInviteRequestBody friendInvite) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.createFriendRequest(user, friendInvite);
    }

    @PutMapping("/friends")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public void answerFriendRequest(@RequestBody FriendAnswerRequestBody friendAnswer) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.acceptFriendRequest(user, friendAnswer);
    }

    @GetMapping("/profile/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileInfo getUserInfo(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        Set<GroupInfo> groupInfo = user.getGroups().stream()
                .map(group -> groupService.getGroupInfo(group.getGroupId())).collect(Collectors.toSet());
        return new UserProfileInfo(user, groupInfo);
    }
}