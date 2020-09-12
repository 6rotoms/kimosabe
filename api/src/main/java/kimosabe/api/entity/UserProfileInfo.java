package kimosabe.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import kimosabe.api.model.Group;
import kimosabe.api.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter @NoArgsConstructor
public class UserProfileInfo extends UserInfo {
    @JsonProperty("userInfo")
    private UserInfo userInfo;
    @JsonProperty("groups")
    private Set<GroupInfo> groups;

    @JsonProperty("friends")
    private Set<UserInfo> friends;

    public UserProfileInfo(User u, Set<GroupInfo> groups) {
        this.userInfo = new UserInfo(u);
        this.groups = groups;
        this.friends = u.getFriends().stream().map(UserInfo::new).collect(Collectors.toSet());
    }
}
