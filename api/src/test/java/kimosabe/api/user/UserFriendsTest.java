package kimosabe.api.user;

import kimosabe.api.AbstractBaseIntegrationTest;
import kimosabe.api.entity.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class UserFriendsTest extends AbstractBaseIntegrationTest {
    @BeforeEach
    public void setup() {
        this.baseUrl = "http://localhost:" + randomServerPort + "/user/profile/{username}/friends";
    }

    @Test
    @DisplayName("valid username returns 200")
    public void whenUsernameValid_thenReturn200OK() {
        // Act
        ResponseEntity<UserInfo[]> response = restTemplate.getForEntity(baseUrl, UserInfo[].class, "user1");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
