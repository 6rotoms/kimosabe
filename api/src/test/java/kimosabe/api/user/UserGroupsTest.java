package kimosabe.api.user;

import kimosabe.api.TestUserUtils;
import kimosabe.api.entity.GroupInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserGroupsTest {
    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;
    String baseUrl;

    @BeforeEach
    public void setup() {
        this.baseUrl = "http://localhost:" + randomServerPort + "/user/profile/{username}/groups";
        TestUserUtils.user1CreateNewGroupBaldursGate(restTemplate, randomServerPort);
    }

    @Test
    @DisplayName("valid username returns 200")
    public void whenUsernameValid_ThenReturn200OK() {
        ResponseEntity<GroupInfo[]> response = restTemplate.getForEntity(baseUrl, GroupInfo[].class, "user1");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("no groups returns empty array")
    public void whenNoGroups_ThenReturnEmpty() {
        ResponseEntity<GroupInfo[]> response = restTemplate.getForEntity(baseUrl, GroupInfo[].class, "user1");
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @DisplayName("some groups returns array of group info")
    public void whenSomeGroups_ThenReturnGroups() {
        TestUserUtils.user1JoinBaldursGateGroup(restTemplate, randomServerPort);
        ResponseEntity<GroupInfo[]> response = restTemplate.getForEntity(baseUrl, GroupInfo[].class, "user1");
        assertThat(response.getBody().length).isEqualTo(1);
    }
}
