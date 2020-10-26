package kimosabe.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import kimosabe.api.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class UserInfo {
    @JsonProperty("username")
    private String username;
    @JsonProperty("profileIcon")
    private String profileIcon;

    public UserInfo(User user) {
        this.username = user.getUsername();
        this.profileIcon = "icondata";
    }
}