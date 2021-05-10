package kimosabe.api.auth;

import kimosabe.api.AbstractBaseIntegrationTest;
import kimosabe.api.entity.LoginDetailsRequestBody;
import kimosabe.api.entity.RegistrationDetailsRequestBody;
import kimosabe.api.model.RoleName;
import kimosabe.api.model.User;
import kimosabe.api.model.VerificationCode;
import kimosabe.api.repository.UserRepository;
import kimosabe.api.repository.VerificationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthTest extends AbstractBaseIntegrationTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationCodeRepository codeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.mail.host}")
    String mailHost;

    String mailHostUrl;

    @BeforeEach
    public void setup() {
        super.cleanUpDb();
        baseUrl = "http://localhost:"+ randomServerPort + "/auth";
        mailHostUrl = String.format("http://%s:8025", mailHost);
        restTemplate.delete(mailHostUrl + "/api/v1/messages");
    }

    @Test
    @DisplayName("valid login returns 200 and updates last login")
    void whenLoginValid_ThenLastLoginTimeShouldBeSet() {
        // Arrange
        LoginDetailsRequestBody requestBody = new LoginDetailsRequestBody();
        requestBody.setUsername("user1");
        requestBody.setPassword("password1");
        HttpEntity<LoginDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        Instant start = Instant.now();
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + "/login", request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Optional<User> entity = userRepository.findByUsername("user1");
        assert(entity.isPresent());
        assertThat(entity.get().getLastLogin()).isBetween(start, Instant.now());
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
        RegistrationDetailsRequestBody requestBody = new RegistrationDetailsRequestBody();
        requestBody.setUsername("user1");
        requestBody.setEmail("user3@gmail.com");
        requestBody.setPassword("password2");
        HttpEntity<RegistrationDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + "/register", request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("invalid username registration request returns 400")
    void whenRegistrationUsernameInvalid_ThenReturn400BadRequest() {
        // Arrange
        RegistrationDetailsRequestBody requestBody = new RegistrationDetailsRequestBody();
        requestBody.setUsername("u");
        requestBody.setEmail("user3@gmail.com");
        requestBody.setPassword("password2");
        HttpEntity<RegistrationDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + "/register", request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("bad password registration request returns 400")
    void whenRegistrationPasswordInvalid_ThenReturn400BadRequest() {
        // Arrange
        RegistrationDetailsRequestBody requestBody = new RegistrationDetailsRequestBody();
        requestBody.setUsername("user3");
        requestBody.setEmail("user3@gmail.com");
        requestBody.setPassword("p");
        HttpEntity<RegistrationDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + "/register", request, String.class);

        // Assert
        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("valid registration request returns 200, creates user, and sends email")
    void whenRegistrationValid_ThenUserShouldBeCreated() {
        // Arrange
        RegistrationDetailsRequestBody requestBody = new RegistrationDetailsRequestBody();
        requestBody.setUsername("user3");
        requestBody.setEmail("user3@gmail.com");
        requestBody.setPassword("password1");
        HttpEntity<RegistrationDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + "/register", request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Optional<User> userEntity = userRepository.findByUsername("user3");
        assert(userEntity.isPresent());
        User user = userEntity.get();
        assert(user.getUsername().equals("user3"));
        assert(passwordEncoder.matches("password1", user.getPassword()));
        assertTrue(user.hasRole(RoleName.ROLE_USER));
        String response = restTemplate.getForObject(mailHostUrl+"/api/v1/messages", String.class);
        assertThat(response).containsSubsequence(String.format("\"To\":[\"user3@gmail.com\"]"));
        Pattern pattern = Pattern.compile("https://kimosabe.cyou/verify\\?token=(.*?)\\\\", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);
        assertTrue(matcher.find());
        String token = matcher.group(1);
        Optional<VerificationCode> codeEntity = codeRepository.findById(token);
        assertTrue(codeEntity.isPresent());
        VerificationCode code = codeEntity.get();
        assertEquals(code.getUser().getUsername(), "user3");
    }

    @Test
    @DisplayName("valid verification request returns 200 and verifies user")
    void whenVerificationValid_thenUserShouldBeVerified() {
        // Arrange
        RegistrationDetailsRequestBody requestBody = new RegistrationDetailsRequestBody();
        requestBody.setUsername("user3");
        requestBody.setEmail("user3@gmail.com");
        requestBody.setPassword("password1");
        HttpEntity<RegistrationDetailsRequestBody> request = new HttpEntity<>(requestBody);

        // Act
        restTemplate.postForEntity(baseUrl + "/register", request, String.class);
        String jsonEmail = restTemplate.getForObject(mailHostUrl+"/api/v1/messages", String.class);
        Pattern pattern = Pattern.compile("https://kimosabe.cyou/verify\\?token=(.*?)\\\\", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(jsonEmail);
        assertTrue(matcher.find());
        String token = matcher.group(1);
        ResponseEntity<String> response = restTemplate.postForEntity(String.format(baseUrl + "/verify?token=%s", token), request, String.class);

        // Assert
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        Optional<User> userEntity = userRepository.findByUsername("user3");
        assert(userEntity.isPresent());
        User user = userEntity.get();
        assertTrue(user.hasRole(RoleName.ROLE_VERIFIED));
    }
}
