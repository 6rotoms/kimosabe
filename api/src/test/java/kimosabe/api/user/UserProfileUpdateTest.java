package kimosabe.api.user;

import kimosabe.api.AbstractBaseIntegrationTest;
import kimosabe.api.TestUserConstants;
import kimosabe.api.TestUserUtils;
import kimosabe.api.entity.UserProfileInfo;
import kimosabe.api.model.User;
import kimosabe.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserProfileUpdateTest extends AbstractBaseIntegrationTest {
    @Autowired
    UserRepository userRepository;

    HttpHeaders headers;

    @BeforeEach
    public void setup() {
        super.cleanUpDb();
        baseUrl = "http://localhost:" + randomServerPort + "/user/profile";
        headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
    }

    @Test
    @DisplayName("valid update returns 200")
    public void whenValidUserUpdateWithAll_thenReturn200OK() {
        // Arrange
        HttpEntity<UserProfileInfo> request = new HttpEntity<>(TestUserConstants.getUser1ASL(), headers);

        // Act
        ResponseEntity<String> resp = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                request,
                String.class
        );

        // Assert
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("valid update changes proper fields in db")
    public void whenValidUserUpdateWithAll_thenDbUpdated() {
        // Arrange
        UserProfileInfo expected = TestUserConstants.getUser1ASL();
        HttpEntity<UserProfileInfo> request = new HttpEntity<>(expected, headers);

        // Act
        restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                request,
                String.class
        );

        // Assert
        Optional<User> data = userRepository.findByUsername("user1");
        assertThat(data).isPresent();
        User user = data.get();
        assertThat(user.getAge()).isEqualTo(expected.getAge());
        assertThat(user.getGender()).isEqualTo(expected.getGender());
        assertThat(user.getLocation()).isEqualTo(expected.getLocation());
        assertThat(user.getBiography()).isEqualTo(expected.getBiography());
    }

    @Test
    @DisplayName("valid update when biography missing")
    public void whenBiographyMissing_thenDbUpdated() {
        // Arrange
        UserProfileInfo expected = TestUserConstants.getUser1ASL();
        expected.setBiography(null);
        HttpEntity<UserProfileInfo> request = new HttpEntity<>(expected, headers);

        // Act
        restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                request,
                String.class
        );

        // Assert
        Optional<User> data = userRepository.findByUsername("user1");
        assertThat(data).isPresent();
        User user = data.get();
        assertThat(user.getAge()).isEqualTo(expected.getAge());
        assertThat(user.getGender()).isEqualTo(expected.getGender());
        assertThat(user.getLocation()).isEqualTo(expected.getLocation());
        assertThat(user.getBiography()).isNull();
    }

    @Test
    @DisplayName("valid update when only biography present")
    public void whenASLMissing_thenDbUpdated() {
        // Arrange
        UserProfileInfo expected = TestUserConstants.getUser1ASL();
        expected.setAge(null);
        expected.setGender(null);
        expected.setLocation(null);
        HttpEntity<UserProfileInfo> request = new HttpEntity<>(expected, headers);

        // Act
        restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                request,
                String.class
        );

        // Assert
        Optional<User> data = userRepository.findByUsername("user1");
        assertThat(data).isPresent();
        User user = data.get();
        assertThat(user.getAge()).isNull();
        assertThat(user.getGender()).isNull();
        assertThat(user.getLocation()).isNull();
        assertThat(user.getBiography()).isEqualTo(expected.getBiography());
    }

    @Test
    @DisplayName("valid update when only some ASL is present")
    public void whenSomeASLMissing_thenDbUpdated() {
        // Arrange
        UserProfileInfo expected = TestUserConstants.getUser1ASL();
        expected.setAge(null);
        expected.setBiography(null);
        HttpEntity<UserProfileInfo> request = new HttpEntity<>(expected, headers);

        // Act
        restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                request,
                String.class
        );

        // Assert
        Optional<User> data = userRepository.findByUsername("user1");
        assertThat(data).isPresent();
        User user = data.get();
        assertThat(user.getAge()).isNull();
        assertThat(user.getGender()).isEqualTo(expected.getGender());
        assertThat(user.getLocation()).isEqualTo(expected.getLocation());
        assertThat(user.getBiography()).isNull();
    }
}
