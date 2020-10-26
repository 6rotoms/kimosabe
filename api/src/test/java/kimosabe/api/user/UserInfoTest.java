package kimosabe.api.user;

import kimosabe.api.AbstractBaseIntegrationTest;
import kimosabe.api.TestUserConstants;
import kimosabe.api.TestUserUtils;
import kimosabe.api.entity.UserProfileInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class UserInfoTest extends AbstractBaseIntegrationTest {
    @BeforeEach
    public void setup() {
        this.baseUrl = "http://localhost:" + randomServerPort + "/user/profile/{username}";
    }

    @Test
    @DisplayName("valid username returns 200")
    public void whenUsernameValid_ThenReturn200OK() {
        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class, "user1");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("invalid username returns 404")
    public void whenUsernameInvalid_thenReturn404() {
        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class, "invalid_username");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("valid username returns correct object")
    public void whenUsernameValid_ThenReturnProperObject() {
        // Arrange
        TestUserUtils.user1UpdateASLBiography(restTemplate, randomServerPort);
        UserProfileInfo expected = TestUserConstants.getUser1ASL();
        // Act
        ResponseEntity<UserProfileInfo> response = restTemplate.getForEntity(baseUrl, UserProfileInfo.class, "user1");

        // Assert
        UserProfileInfo body = response.getBody();
        assertThat(body.getUserInfo().getUsername()).isEqualTo(expected.getUserInfo().getUsername());
        assertThat(body.getAge()).isEqualTo(expected.getAge());
        assertThat(body.getBiography()).isEqualTo(expected.getBiography());
        assertThat(body.getLocation()).isEqualTo(expected.getLocation());
        assertThat(body.getGender()).isEqualTo(expected.getGender());
    }
}
