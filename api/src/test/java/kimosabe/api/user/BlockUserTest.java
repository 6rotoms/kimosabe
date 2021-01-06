package kimosabe.api.user;

import kimosabe.api.AbstractBaseIntegrationTest;
import kimosabe.api.TestUserConstants;
import kimosabe.api.TestUserUtils;
import kimosabe.api.model.RelationshipStatus;
import kimosabe.api.model.UserRelationshipId;
import kimosabe.api.repository.UserRelationshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockUserTest extends AbstractBaseIntegrationTest {
    @Autowired
    UserRelationshipRepository userRelationshipRepository;

    @BeforeEach
    public void setup() {
        // Arrange
        baseUrl = "http://localhost:" + randomServerPort + "/user/block/{username}";
    }

    @Test
    @DisplayName("successful block user returns 200")
    public void whenBlockSuccessful_thenReturn200() {
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class, "user2");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("successful block user adds BLOCKED relationship")
    public void whenBlockSuccessful_thenAddBlocked() {
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class, "user2");

        // Assert
        assertThat(userRelationshipRepository.findByIdAndRelationshipStatus(
                new UserRelationshipId(TestUserConstants.user1Id,
                        TestUserConstants.user2Id), RelationshipStatus.BLOCKED)).isNotEmpty();
    }

    @Test
    @DisplayName("successful block user removes PENDING relationship")
    public void whenBlockSuccessful_thenRemovePending() {
        // Arrange
        HttpHeaders headers = TestUserUtils.user1RequestFriendUser2(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class, "user2");

        // Assert
        assertThat(userRelationshipRepository.findByIdAndRelationshipStatus(
                new UserRelationshipId(TestUserConstants.user1Id,
                        TestUserConstants.user2Id), RelationshipStatus.PENDING)).isEmpty();
    }

    @Test
    @DisplayName("successful block user removes ACCEPTED relationship")
    public void whenBlockSuccessful_thenRemoveAccepted() {
        // Arrange
        HttpHeaders headers = TestUserUtils.user2AcceptUser1FriendRequest(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class, "user1");

        // Assert
        assertThat(userRelationshipRepository.findByIdAndRelationshipStatus(
                new UserRelationshipId(TestUserConstants.user1Id,
                        TestUserConstants.user2Id), RelationshipStatus.ACCEPTED)).isEmpty();
    }

    @Test
    @DisplayName("block user on self returns 403")
    public void whenBlockSelf_thenReturn403() {
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class, "user1");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
