package kimosabe.api.auth;

import kimosabe.api.entity.LoginDetailsRequestBody;
import kimosabe.api.model.Role;
import kimosabe.api.model.RoleName;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthTest {
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
        baseUrl = "http://localhost:"+ randomServerPort + "/auth";
    }

    @Test
    @DisplayName("valid login request returns 200")
    void whenLoginValid_ThenReturn200OK() {
        // Arrange
        LoginDetailsRequestBody requestBody = new LoginDetailsRequestBody();
        requestBody.setUsername("user1");
        requestBody.setPassword("password1");
        HttpEntity<LoginDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + "/login", request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("invalid username login request returns 403")
    void whenLoginUsernameNotValid_thenReturn403Forbidden() {
        // Arrange
        LoginDetailsRequestBody requestBody = new LoginDetailsRequestBody();
        requestBody.setUsername("user");
        requestBody.setPassword("password1");
        HttpEntity<LoginDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + "/login", request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("invalid password login request returns 403")
    void whenLoginPasswordNotValid_thenReturn403Forbidden() {
        // Arrange
        LoginDetailsRequestBody requestBody = new LoginDetailsRequestBody();
        requestBody.setUsername("user1");
        requestBody.setPassword("password2");
        HttpEntity<LoginDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + "/login", request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("taken username registration request returns 409")
    void whenRegistrationUsernameTaken_ThenReturn409Conflict() {
        // Arrange
        LoginDetailsRequestBody requestBody = new LoginDetailsRequestBody();
        requestBody.setUsername("user1");
        requestBody.setPassword("password2");
        HttpEntity<LoginDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + "/register", request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("invalid username registration request returns 400")
    void whenRegistrationUsernameInvalid_ThenReturn400BadRequest() {
        // Arrange
        LoginDetailsRequestBody requestBody = new LoginDetailsRequestBody();
        requestBody.setUsername("u");
        requestBody.setPassword("password2");
        HttpEntity<LoginDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + "/register", request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("bad password registration request returns 400")
    void whenRegistrationPasswordInvalid_ThenReturn400BadRequest() {
        // Arrange
        LoginDetailsRequestBody requestBody = new LoginDetailsRequestBody();
        requestBody.setUsername("user1");
        requestBody.setPassword("p");
        HttpEntity<LoginDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + "/register", request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("valid registration request returns 200")
    void whenRegistrationValid_ThenReturn200OK() {
        // Arrange
        LoginDetailsRequestBody requestBody = new LoginDetailsRequestBody();
        requestBody.setUsername("user3");
        requestBody.setPassword("password1");
        HttpEntity<LoginDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + "/register", request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("valid registration request creates user with proper roles")
    void whenRegistrationValid_ThenUserShouldBeCreated() {
        // Arrange
        LoginDetailsRequestBody requestBody = new LoginDetailsRequestBody();
        requestBody.setUsername("user3");
        requestBody.setPassword("password1");
        HttpEntity<LoginDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        restTemplate.postForEntity(baseUrl + "/register", request, String.class);

        // Assert
        Optional<User> entity = userRepository.findByUsername("user3");
        assert(entity.isPresent());
        User user = entity.get();
        assert(user.getUsername().equals("user3"));
        assert(passwordEncoder.matches("password1", user.getPassword()));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(new Role(RoleName.ROLE_USER));
        assertEquals(user.getRoles(), userRoles);
    }
}
