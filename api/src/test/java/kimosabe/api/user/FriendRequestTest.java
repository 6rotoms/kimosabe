package kimosabe.api.user;

import kimosabe.api.AbstractBaseIntegrationTest;
import kimosabe.api.TestUserConstants;
import kimosabe.api.TestUserUtils;
import kimosabe.api.entity.FriendInviteRequestBody;
import kimosabe.api.model.RelationshipStatus;
import kimosabe.api.model.UserRelationship;
import kimosabe.api.model.UserRelationshipId;
import kimosabe.api.repository.UserRelationshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class FriendRequestTest extends AbstractBaseIntegrationTest {
    @Autowired
    UserRelationshipRepository userRelationshipRepository;

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:"+ randomServerPort + "/user/friends";
    }

    @Test
    @DisplayName("friend request returns 200")
    public void whenFriendInviteValid_thenReturn200OK() {
        // Arrange
        FriendInviteRequestBody requestBody= new FriendInviteRequestBody();
        requestBody.setFriendUsername("user2");
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<FriendInviteRequestBody> request = new HttpEntity<>(requestBody, headers);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl, request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("valid friend request adds pending relationship")
    public void whenFriendInviteValid_thenAddFriendRelationship() {
        // Arrange
        FriendInviteRequestBody requestBody= new FriendInviteRequestBody();
        requestBody.setFriendUsername("user2");
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<FriendInviteRequestBody> request = new HttpEntity<>(requestBody, headers);
        // Act
        restTemplate.postForEntity(baseUrl, request, String.class);

        // Assert
        UserRelationshipId id = new UserRelationshipId(TestUserConstants.user1Id, TestUserConstants.user2Id);
        Optional<UserRelationship> result = userRelationshipRepository
                .findByIdAndRelationshipStatus(id, RelationshipStatus.PENDING);
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("duplicate valid friend requests only accepts one")
    public void whenFriendInviteSentTwice_thenOnlyOneShouldExist() {
        // Arrange
        FriendInviteRequestBody requestBody= new FriendInviteRequestBody();
        requestBody.setFriendUsername("user2");
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<FriendInviteRequestBody> request = new HttpEntity<>(requestBody, headers);
        // Act
        restTemplate.postForEntity(baseUrl, request, String.class);
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl, request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<UserRelationshipId> ids = new ArrayList<>();
        ids.add(new UserRelationshipId(TestUserConstants.user1Id, TestUserConstants.user2Id));
        assertThat(userRelationshipRepository.findAllById(ids).size()).isEqualTo(1);
    }
}
