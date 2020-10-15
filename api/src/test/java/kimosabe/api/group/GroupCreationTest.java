package kimosabe.api.group;

import kimosabe.api.TestUserUtils;
import kimosabe.api.model.Group;
import kimosabe.api.repository.GroupRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GroupCreationTest {
    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;
    String baseUrl;

    @Autowired
    GroupRepository groupRepository;

    HttpHeaders headers;

    @BeforeEach
    public void setup() {
        // Arrange
        this.baseUrl = "http://localhost:" + randomServerPort + "/groups";
        headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
    }

    @Test
    @DisplayName("Test if valid group creation returns 200")
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
    @DisplayName("Test if valid group creation properly initializes all entries")
    @Transactional
    public void whenGroupIdValid_thenExpectGroupObjectToBeCreated() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        restTemplate.postForEntity(this.baseUrl + "/baldur-s-gate-enhanced-edition", request, String.class);
        Optional<Group> result = groupRepository.findById("baldur-s-gate-enhanced-edition");

        // Assert
        assertThat(result.isPresent()).isTrue();
        Group group = result.get();
        assertThat(group.getGroupId()).isEqualTo("baldur-s-gate-enhanced-edition");
        assertThat(group.getName()).isEqualTo("Baldur's Gate: Enhanced Edition");
        assertThat(group.getMembers()).isEmpty();
    }

    @Test
    @DisplayName("Test if invalid group id returns 404")
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
    @DisplayName("Test if duplicate group id returns 409")
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
