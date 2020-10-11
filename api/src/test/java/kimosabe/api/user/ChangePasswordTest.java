package kimosabe.api.user;

import kimosabe.api.TestUserUtils;
import kimosabe.api.entity.ChangePasswordRequestBody;
import kimosabe.api.model.User;
import kimosabe.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChangePasswordTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @LocalServerPort
    int randomServerPort;
    String baseUrl;

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:"+ randomServerPort + "/user/changePassword";
    }

    @Test
    @DisplayName("valid password change request returns 200 status code")
    public void whenChangePasswordValid_thenReturn200OK() {
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        ChangePasswordRequestBody requestBody = new ChangePasswordRequestBody();
        requestBody.setOldPassword("password1");
        requestBody.setNewPassword("newPassword");
        HttpEntity<ChangePasswordRequestBody> request = new HttpEntity<>(requestBody, headers);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl, request, String.class);

        // Assert
        assert(result.getStatusCode()).equals(HttpStatus.OK);
    }

    @Test
    @DisplayName("valid password change request changes password in db")
    public void whenChangePasswordValid_thenPasswordShouldBeChanged() {
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        ChangePasswordRequestBody requestBody = new ChangePasswordRequestBody();
        requestBody.setOldPassword("password1");
        requestBody.setNewPassword("newPassword");
        HttpEntity<ChangePasswordRequestBody> request = new HttpEntity<>(requestBody, headers);

        // Act
        restTemplate.postForEntity(baseUrl, request, String.class);

        // Assert
        Optional<User> user = userRepository.findByUsername("user1");
        assert(user.isPresent());
        assertThat(passwordEncoder.matches("newPassword", user.get().getPassword())).isTrue();
    }

    @Test
    @DisplayName("bad old password returns 403 status code")
    public void whenChangePasswordWrongOldPassword_thenReturn403Forbidden() {
        // Arrange
        ChangePasswordRequestBody requestBody = new ChangePasswordRequestBody();
        requestBody.setOldPassword("wrongPassword");
        requestBody.setNewPassword("newPassword");
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<ChangePasswordRequestBody> request = new HttpEntity<>(requestBody, headers);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl, request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("bad new password returns 400 status code")
    public void whenChangePasswordBadNewPassword_thenReturn400BadRequest() {
        // Arrange
        ChangePasswordRequestBody requestBody = new ChangePasswordRequestBody();
        requestBody.setOldPassword("password1");
        requestBody.setNewPassword("na");
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<ChangePasswordRequestBody> request = new HttpEntity<>(requestBody, headers);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl, request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}