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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GroupInfoTest {
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
        this.baseUrl = "http://localhost:"+randomServerPort+"/groups/";
        headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
    }

    public void createGroup() {
        // Arrange
        Group newGroup = new Group();
        newGroup.setGroupId("baldur-s-gate-enhanced-edition");
        newGroup.setName("Baldur's Gate: Enhanced Edition");
        groupRepository.save(newGroup);
    }

    @Test
    @DisplayName("valid group get returns 200")
    public void whenGroupExists_thenReturn200OK() {
        // Arrange
        createGroup();
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "baldur-s-gate-enhanced-edition",
                HttpMethod.GET, request, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("valid group returns proper info")
    public void whenGroupExists_thenReturnGroupInfo() {
        // Arrange
        createGroup();
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "baldur-s-gate-enhanced-edition",
                HttpMethod.GET, request, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("invalid group id returns 404")
    public void whenGroupDoesNotExist_thenReturn404() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "baldur-s-gate-enhanced-edition",
                HttpMethod.GET, request, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
