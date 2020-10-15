package kimosabe.api.user;

import kimosabe.api.entity.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserInfoTest {
    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;
    String baseUrl;

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
        // Act
        ResponseEntity<UserInfo> response = restTemplate.getForEntity(baseUrl, UserInfo.class, "user1");

        // Assert
        assertThat(response.getBody().getUsername()).isEqualTo("user1");
    }
}
