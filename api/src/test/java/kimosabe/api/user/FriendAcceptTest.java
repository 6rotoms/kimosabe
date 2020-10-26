package kimosabe.api.user;

import kimosabe.api.AbstractBaseIntegrationTest;
import kimosabe.api.TestUserConstants;
import kimosabe.api.TestUserUtils;
import kimosabe.api.entity.FriendAnswerRequestBody;
import kimosabe.api.model.RelationshipStatus;
import kimosabe.api.model.UserRelationship;
import kimosabe.api.model.UserRelationshipId;
import kimosabe.api.repository.UserRelationshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class FriendAcceptTest extends AbstractBaseIntegrationTest {
    @Autowired
    UserRelationshipRepository userRelationshipRepository;

    @BeforeEach
    public void user1FriendRequestUser2() {
        // Arrange
        baseUrl = "http://localhost:"+ randomServerPort + "/user/friends";
        TestUserUtils.user1RequestFriendUser2(restTemplate, randomServerPort);
    }

    @Test
    @DisplayName("valid friend accept returns 200")
    public void whenFriendAcceptValid_thenReturn200OK() {
        // Arrange
        FriendAnswerRequestBody requestBody= new FriendAnswerRequestBody();
        requestBody.setFrom("user1");
        requestBody.setAccept(true);
        HttpHeaders headers = TestUserUtils.loginUser2(restTemplate, randomServerPort);
        HttpEntity<FriendAnswerRequestBody> request = new HttpEntity<>(requestBody, headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.PUT, request, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("valid friend accept adds accepted relationship")
    public void whenFriendAcceptValid_thenChangeRelationshipToPending() {
        // Arrange
        FriendAnswerRequestBody requestBody= new FriendAnswerRequestBody();
        requestBody.setFrom("user1");
        requestBody.setAccept(true);
        HttpHeaders headers = TestUserUtils.loginUser2(restTemplate, randomServerPort);
        HttpEntity<FriendAnswerRequestBody> request = new HttpEntity<>(requestBody, headers);

        // Act
        restTemplate.exchange(baseUrl, HttpMethod.PUT, request, String.class);

        // Assert
        Optional<UserRelationship> accepted = userRelationshipRepository
                .findByIdAndRelationshipStatus(new UserRelationshipId(TestUserConstants.user1Id, TestUserConstants.user2Id),
                        RelationshipStatus.ACCEPTED);
        Optional<UserRelationship> pending = userRelationshipRepository
                .findByIdAndRelationshipStatus(new UserRelationshipId(TestUserConstants.user1Id, TestUserConstants.user2Id),
                        RelationshipStatus.PENDING);
        assertThat(accepted.isPresent()).isTrue();
        assertThat(pending.isPresent()).isFalse();
    }

    @Test
    @DisplayName("missing accept in answer defaults to false")
    public void whenFriendAcceptMissingAccept_thenDeny() {
        // Arrange
        FriendAnswerRequestBody requestBody= new FriendAnswerRequestBody();
        requestBody.setFrom("user1");
        HttpHeaders headers = TestUserUtils.loginUser2(restTemplate, randomServerPort);
        HttpEntity<FriendAnswerRequestBody> request = new HttpEntity<>(requestBody, headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.PUT, request, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("valid friend deny removes pending relationship")
    public void whenFriendDenyValid_thenRemoveRelationship() {
        // Arrange
        FriendAnswerRequestBody requestBody= new FriendAnswerRequestBody();
        requestBody.setFrom("user1");
        requestBody.setAccept(false);
        HttpHeaders headers = TestUserUtils.loginUser2(restTemplate, randomServerPort);
        HttpEntity<FriendAnswerRequestBody> request = new HttpEntity<>(requestBody, headers);

        // Act
        restTemplate.exchange(baseUrl, HttpMethod.PUT, request, String.class);

        // Assert
        Optional<UserRelationship> accepted = userRelationshipRepository
                .findByIdAndRelationshipStatus(new UserRelationshipId(TestUserConstants.user1Id, TestUserConstants.user2Id),
                        RelationshipStatus.ACCEPTED);
        Optional<UserRelationship> pending = userRelationshipRepository
                .findByIdAndRelationshipStatus(new UserRelationshipId(TestUserConstants.user1Id, TestUserConstants.user2Id),
                        RelationshipStatus.PENDING);
        assertThat(accepted.isPresent()).isFalse();
        assertThat(pending.isPresent()).isFalse();
    }
}
