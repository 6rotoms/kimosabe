package kimosabe.api.group;

import kimosabe.api.AbstractBaseIntegrationTest;
import kimosabe.api.TestUserUtils;
import kimosabe.api.entity.ExceptionResponse;
import kimosabe.api.entity.GroupInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

public class GroupInfoTest extends AbstractBaseIntegrationTest {
    HttpHeaders headers;

    @BeforeEach
    public void setup() {
        // Arrange
        super.cleanUpDb();
        this.baseUrl = "http://localhost:"+randomServerPort+"/groups/";
        headers = TestUserUtils.loginUser1(restTemplate, randomServerPort);
    }

    @Test
    @DisplayName("valid group get returns 200")
    public void whenGroupExists_thenReturn200OK() {
        // Arrange
        TestUserUtils.user1CreateNewGroupBaldursGate(restTemplate, randomServerPort);
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
        TestUserUtils.user1CreateNewGroupBaldursGate(restTemplate, randomServerPort);

        // Act
        ResponseEntity<GroupInfo> response = restTemplate.getForEntity(baseUrl+ "baldur-s-gate-enhanced-edition", GroupInfo.class);

        // Assert
        GroupInfo body = response.getBody();
        assertThat(body.getGroupId()).isEqualTo("baldur-s-gate-enhanced-edition");
        assertThat(body.getGroupName()).isEqualTo("Baldur's Gate: Enhanced Edition");
    }

    @Test
    @DisplayName("invalid group id returns 404 with error")
    public void whenGroupDoesNotExist_thenReturn404() {

        // Act
        ResponseEntity<ExceptionResponse> response = restTemplate.getForEntity(baseUrl + "baldur-s-gate-enhanced-edition", ExceptionResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ExceptionResponse body = response.getBody();
        assertThat(body.getMessage()).isEqualTo("Group is missing from database");
    }
}
