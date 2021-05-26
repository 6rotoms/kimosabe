package kimosabe.api.user;

import kimosabe.api.AbstractBaseIntegrationTest;
import kimosabe.api.TestUserConstants;
import kimosabe.api.TestUserUtils;
import kimosabe.api.model.UserRelationshipId;
import kimosabe.api.repository.UserRelationshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

public class FriendDeleteTest extends AbstractBaseIntegrationTest {
    @Autowired
    UserRelationshipRepository userRelationshipRepository;

    @BeforeEach
    public void setup() {
        // Arrange
        super.cleanUpDb();
        baseUrl = "http://localhost:"+ randomServerPort + "/user/friends/{username}";
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
                baseUrl,
                    HttpMethod.DELETE, request,
                    String.class, "user2");

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
        restTemplate.exchange(
                baseUrl,
                HttpMethod.DELETE, request,
                String.class, "user2");

        // Assert
        assertThat(userRelationshipRepository.findById(
                new UserRelationshipId(TestUserConstants.user1Id,
                        TestUserConstants.user2Id))).isEmpty();
    }

    @Test
    @DisplayName("target deleting friend deletes relationship")
    public void whenTargetDelete_thenDeleteFriend() {
        // Arrange
        HttpHeaders headers = TestUserUtils.user2AcceptUser1FriendRequest(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.DELETE, request,
                String.class, "user1");

        // Assert
        assertThat(userRelationshipRepository.findById(
                new UserRelationshipId(TestUserConstants.user1Id,
                        TestUserConstants.user2Id))).isEmpty();
    }

    @Test
    @DisplayName("deleting non-existent relationship returns 404")
    public void whenRelationshipDoesNotExist_thenReturn404() {
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser2(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.DELETE, request,
                String.class, "user1");

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
                baseUrl,
                HttpMethod.DELETE, request,
                String.class, "user3");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
