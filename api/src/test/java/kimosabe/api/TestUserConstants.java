package kimosabe.api;

import kimosabe.api.entity.UserInfo;
import kimosabe.api.entity.UserProfileInfo;

import java.util.UUID;

public class TestUserConstants {
    public static final UUID user1Id = UUID.fromString("41096c69-47db-4fdb-9a84-bef10e571546");
    public static final UUID user2Id = UUID.fromString("b72cfbb3-565d-4711-81ad-e6cdf1f349c1");

    public static UserProfileInfo getUser1ASL() {
        UserProfileInfo user1Updated = new UserProfileInfo();
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("user1");
        user1Updated.setUserInfo(userInfo);
        user1Updated.setAge(18);
        user1Updated.setGender("Female");
        user1Updated.setLocation("California");
        user1Updated.setBiography("Hello!");
        return user1Updated;
    }
}
