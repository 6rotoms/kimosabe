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
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final GroupService groupService;
    private final SessionRegistry sessionRegistry;

    @Autowired
    public UserController(
            UserService userService,
            GroupService groupService,
            SessionRegistry sessionRegistry
    ) {
        this.userService = userService;
        this.groupService = groupService;
        this.sessionRegistry = sessionRegistry;
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody ChangePasswordRequestBody changePassword, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        if (userService.checkPassword(user, changePassword.getOldPassword())) {
            userService.changePassword(user, changePassword.getNewPassword());
        } else {
            throw new IncorrectPasswordException();
        }
    }

    @PostMapping("/friends")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public void requestFriend(@RequestBody FriendInviteRequestBody friendInvite, Principal principal) {
        String user = principal.getName();
        userService.createFriendRequest(user, friendInvite);
    }

    @PutMapping("/friends")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public void answerFriendRequest(@RequestBody FriendAnswerRequestBody friendAnswer) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.acceptFriendRequest(username, friendAnswer);
    }

    @DeleteMapping("/friends/{friendName}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteFriend(@PathVariable String friendName) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.deleteFriend(username, friendName);
    }

//    @GetMapping("/sessions")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @ResponseStatus(HttpStatus.OK)
//    public void getSessions(Principal principal) {
//        String user = principal.getName();
//        sessionRegistry.getAllSessions(principal, true).size();
//        System.out.println(sessionRegistry.getAllSessions(principal, true).size());
//    }

    @GetMapping("/profile/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileInfo getUserInfo(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return new UserProfileInfo(user);
    }

    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserInfo(@RequestBody UserProfileInfo profileInfo, Principal principal) {
        String username = principal.getName();
        userService.updateUserInfo(username, profileInfo);
    }

    @GetMapping("/profile/{username}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Set<UserInfo> getAllFriends(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return user.getFriends().stream().map(UserInfo::new).collect(Collectors.toSet());
    }

    @GetMapping("/profile/{username}/groups")
    @ResponseStatus(HttpStatus.OK)
    public Set<GroupInfo> getAllGroups(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return user.getGroups().stream()
                .map(group -> groupService.getGroupInfo(group.getGroupId())).collect(Collectors.toSet());
    }

    @GetMapping("/profile/{username}/blocked")
    @ResponseStatus(HttpStatus.OK)
    public Set<UserInfo> getAllBlocked(@PathVariable String username){
        User user = userService.getUserByUsername(username);
        return user.getBlocked().stream().map(UserInfo::new).collect(Collectors.toSet());
    }

    @PostMapping("/block/{targetName}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public void blockUser(@PathVariable String targetName, Principal principal){
        String username = principal.getName();
        userService.blockUser(username, targetName);
    }

    @DeleteMapping("/block/{blockedName}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public void unblockUser(@PathVariable String blockedName, Principal principal) {
        String username = principal.getName();
        userService.unblockUser(username, blockedName);
    }
}