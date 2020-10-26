package kimosabe.api.group;

import kimosabe.api.AbstractBaseIntegrationTest;
import kimosabe.api.TestUserUtils;
import kimosabe.api.model.Group;
import kimosabe.api.model.User;
import kimosabe.api.repository.GroupRepository;
import kimosabe.api.repository.UserRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AddUserToGroupTest extends AbstractBaseIntegrationTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    HttpHeaders headers;

    @BeforeEach
    public void setup() {
        this.baseUrl = "http://localhost:" + randomServerPort + "/groups";
        headers = TestUserUtils.user1CreateNewGroupBaldursGate(restTemplate, randomServerPort);
    }

    @Test
    @DisplayName("valid add user to group returns 200")
    public void whenUserAddValid_thenReturn200OK() {
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> result = restTemplate.postForEntity(
                baseUrl+ "/baldur-s-gate-enhanced-edition/join", request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Transactional
    @DisplayName("valid add user to group adds entry in DB")
    public void whenUserAddValid_thenAddEntryToDB() {
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        restTemplate.postForEntity(
                baseUrl+ "/baldur-s-gate-enhanced-edition/join", request, String.class);

        // Assert
        Optional<User> userData = userRepository.findByUsername("user1");
        Optional<Group> groupData = groupRepository.findById("baldur-s-gate-enhanced-edition");
        User user = userData.get();
        assertThat(user.getGroups().size()).isEqualTo(1);
        assertThat(user.getGroups()).contains(groupData.get());
    }

    @Test
    @DisplayName("add user to group second time returns 200")
    public void whenUserAddTwice_thenReturn200OK() {
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        restTemplate.postForEntity(
                baseUrl+ "/baldur-s-gate-enhanced-edition/join", request, String.class);
        ResponseEntity<String> result = restTemplate.postForEntity(
                baseUrl+ "/baldur-s-gate-enhanced-edition/join", request, String.class);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Transactional
    @DisplayName("add user to group second time does not add entry in DB")
    public void whenUserAddTwice_thenAddEntryToDBOnce() {
        // Arrange
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Act
        restTemplate.postForEntity(
                baseUrl+ "/baldur-s-gate-enhanced-edition/join", request, String.class);

        // Assert
        Optional<User> userData = userRepository.findByUsername("user1");
        Optional<Group> groupData = groupRepository.findById("baldur-s-gate-enhanced-edition");
        User user = userData.get();
        assertThat(user.getGroups().size()).isEqualTo(1);
        assertThat(user.getGroups()).containsOnlyOnce(groupData.get());
    }
}
