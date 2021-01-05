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

public class unblockUserTest extends AbstractBaseIntegrationTest {
    @Autowired
    UserRelationshipRepository userRelationshipRepository;

    @BeforeEach
    public void setup() {
        // Arrange
        baseUrl = "http://localhost:"+ randomServerPort + "/user/block/{username}";
    }

    @Test
    @DisplayName("successful unblock of user returns 200")
    public void whenUnblockSuccessful_thenReturn200() {
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class, "user2");

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.DELETE, request,
                String.class, "user2");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("successful unblock of user removes the relationship")
    public void whenUnblockSuccessful_thenRemoveRelationship(){
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class, "user2");

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
    @DisplayName("no blocked relationship exists returns 404")
    public void noBlockedRelationship_thenReturn404(){
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.DELETE, request,
                String.class, "user2");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
