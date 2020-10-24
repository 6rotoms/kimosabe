package kimosabe.api.user;

import kimosabe.api.TestUserUtils;
import kimosabe.api.model.UserRelationshipId;
import kimosabe.api.repository.UserRelationshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FriendDeleteTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRelationshipRepository userRelationshipRepository;

    @LocalServerPort
    int randomServerPort;
    String baseUrl;

    @BeforeEach
    public void setup() {
        // Arrange
        baseUrl = "http://localhost:"+ randomServerPort + "/user/friends";
    }

    @Test
    @DisplayName("successful friend delete returns 200")
    public void whenDeleteSuccessful_thenReturn200OK() {
        // Arrange
        TestUserUtils.user2AcceptUser1FriendRequest(restTemplate, randomServerPort);
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl+ "/user2",
                    HttpMethod.DELETE, request,
                    String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("requester deleting friend deletes relationship")
    public void whenRequesterDelete_thenDeleteFriend() {
        // Arrange
        TestUserUtils.user2AcceptUser1FriendRequest(restTemplate, randomServerPort);
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl+ "/user2",
                HttpMethod.DELETE, request,
                String.class);

        // Assert
        assertThat(userRelationshipRepository.findById(
                new UserRelationshipId(TestUserUtils.user1Id,
                        TestUserUtils.user2Id))).isEmpty();
    }

    @Test
    @DisplayName("target deleting friend deletes relationship")
    public void whenTargetDelete_thenDeleteFriend() {
        // Arrange
        HttpHeaders headers = TestUserUtils.user2AcceptUser1FriendRequest(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl+ "/user1",
                HttpMethod.DELETE, request,
                String.class);

        // Assert
        assertThat(userRelationshipRepository.findById(
                new UserRelationshipId(TestUserUtils.user1Id,
                        TestUserUtils.user2Id))).isEmpty();
    }

    @Test
    @DisplayName("deleting non-existent relationship returns 404")
    public void whenRelationshipDoesNotExist_thenReturn404() {
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser2(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl+ "/user1",
                HttpMethod.DELETE, request,
                String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("non-existent user returns 404")
    public void whenUserDoesNotExist_thenReturn404() {
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser2(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl+ "/user3",
                HttpMethod.DELETE, request,
                String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
