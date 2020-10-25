package kimosabe.api.user;

import kimosabe.api.TestUserUtils;
import kimosabe.api.entity.FriendAnswerRequestBody;
import kimosabe.api.entity.FriendInviteRequestBody;
import kimosabe.api.model.RelationshipStatus;
import kimosabe.api.model.UserRelationship;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FriendAcceptTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRelationshipRepository userRelationshipRepository;

    @LocalServerPort
    int randomServerPort;
    String baseUrl;

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
                .findByIdAndRelationshipStatus(new UserRelationshipId(TestUserUtils.user1Id, TestUserUtils.user2Id),
                        RelationshipStatus.ACCEPTED);
        Optional<UserRelationship> pending = userRelationshipRepository
                .findByIdAndRelationshipStatus(new UserRelationshipId(TestUserUtils.user1Id, TestUserUtils.user2Id),
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
                .findByIdAndRelationshipStatus(new UserRelationshipId(TestUserUtils.user1Id, TestUserUtils.user2Id),
                        RelationshipStatus.ACCEPTED);
        Optional<UserRelationship> pending = userRelationshipRepository
                .findByIdAndRelationshipStatus(new UserRelationshipId(TestUserUtils.user1Id, TestUserUtils.user2Id),
                        RelationshipStatus.PENDING);
        assertThat(accepted.isPresent()).isFalse();
        assertThat(pending.isPresent()).isFalse();
    }
}
