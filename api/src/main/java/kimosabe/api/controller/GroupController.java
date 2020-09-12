package kimosabe.api.controller;

import kimosabe.api.entity.GroupInfo;
import kimosabe.api.model.User;
import kimosabe.api.service.GroupService;
import kimosabe.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private GroupService groupService;
    private UserService userService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @PostMapping("{groupId}/join")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public void joinGroup(@PathVariable String groupId) {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        groupService.addUserToGroup(groupId, user);
    }

    @PostMapping("{groupId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public void createGroup(@PathVariable String groupId) {
        groupService.createGroup(groupId);
    }

    @GetMapping("{groupId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public GroupInfo getGroupInfo(@PathVariable String groupId) {
        return groupService.getGroupInfo(groupId);
    }
}