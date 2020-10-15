package kimosabe.api.user;

import kimosabe.api.TestUserUtils;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FriendRequestTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRelationshipRepository userRelationshipRepository;

    @LocalServerPort
    int randomServerPort;
    String baseUrl;

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:"+ randomServerPort + "/user/friends";
    }

    @Test
    @DisplayName("Test if friend request returns 200")
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
    @DisplayName("Test if valid friend request adds pending relationship")
    public void whenFriendInviteValid_thenAddFriendRelationship() {
        // Arrange
        FriendInviteRequestBody requestBody= new FriendInviteRequestBody();
        requestBody.setFriendUsername("user2");
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<FriendInviteRequestBody> request = new HttpEntity<>(requestBody, headers);
        // Act
        restTemplate.postForEntity(baseUrl, request, String.class);

        // Assert
        UserRelationshipId id = new UserRelationshipId(TestUserUtils.user1Id, TestUserUtils.user2Id);
        Optional<UserRelationship> result = userRelationshipRepository
                .findByIdAndRelationshipStatus(id, RelationshipStatus.PENDING);
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Test if duplicate valid friend requests only accepts one")
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
        ids.add(new UserRelationshipId(TestUserUtils.user1Id, TestUserUtils.user2Id));
        assertThat(userRelationshipRepository.findAllById(ids).size()).isEqualTo(1);
    }
}
