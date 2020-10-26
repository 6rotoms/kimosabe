package kimosabe.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import kimosabe.api.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@NoArgsConstructor @Getter @Setter
public class UserProfileInfo {
    @JsonProperty("userInfo")
    private UserInfo userInfo;
    @JsonProperty("biography")
    private String biography;
    @JsonProperty("age")
    private Integer age;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("lastLogin")
    private Instant lastLogin;
    @JsonProperty("location")
    private String location;

    public UserProfileInfo(User user) {
        this.userInfo = new UserInfo(user);
        this.biography = user.getBiography();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.lastLogin = user.getLastLogin();
        this.location = user.getLocation();
    }
}
