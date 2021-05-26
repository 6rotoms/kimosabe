package kimosabe.api.group;

import kimosabe.api.AbstractBaseIntegrationTest;
import kimosabe.api.TestUserUtils;
import kimosabe.api.model.Group;
import kimosabe.api.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GroupCreationTest extends AbstractBaseIntegrationTest {
    @Autowired
    GroupRepository groupRepository;

    HttpHeaders headers;

    @BeforeEach
    public void setup() {
        // Arrange
        super.cleanUpDb();
        baseUrl = "http://localhost:" + randomServerPort + "/groups";
        headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
    }

    @Test
    @DisplayName("valid group creation returns 200")
    public void whenGroupIdValid_thenReturn200OK() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate
                .postForEntity(this.baseUrl + "/baldur-s-gate-enhanced-edition", request, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("unverified user returns 403")
    public void whenUserUnverified_thenReturn403() {
        // Arrange
        headers = TestUserUtils.loginUnverifiedUser(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate
                .postForEntity(this.baseUrl + "/baldur-s-gate-enhanced-edition", request, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
    @Test
    @DisplayName("valid group creation properly initializes all entries")
    @Transactional
    public void whenGroupIdValid_thenExpectGroupObjectToBeCreated() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        Instant start = Instant.now();
        restTemplate.postForEntity(this.baseUrl + "/baldur-s-gate-enhanced-edition", request, String.class);
        Optional<Group> result = groupRepository.findById("baldur-s-gate-enhanced-edition");

        // Assert
        assertThat(result.isPresent()).isTrue();
        Group group = result.get();
        assertThat(group.getGroupId()).isEqualTo("baldur-s-gate-enhanced-edition");
        assertThat(group.getName()).isEqualTo("Baldur's Gate: Enhanced Edition");
        assertThat(group.getOwner()).isEqualTo("user1");
        assertThat(group.getCreatedDate()).isBetween(start, Instant.now());
        assertThat(group.getMembers()).isEmpty();
    }

    @Test
    @DisplayName("if invalid group id returns 404")
    public void whenGroupIdInvalid_thenReturn404NotFound() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate
                .postForEntity(this.baseUrl + "/baldurs-gate-enhanced-edition", request, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("duplicate group id returns 409")
    public void whenGroupIdDuplicate_thenReturn409Conflict() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        restTemplate
                .postForEntity(this.baseUrl + "/baldur-s-gate-enhanced-edition", request, String.class);
        ResponseEntity<String> response = restTemplate
                .postForEntity(this.baseUrl + "/baldur-s-gate-enhanced-edition", request, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
