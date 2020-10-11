package kimosabe.api.games;

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
public class SearchGamesTest {
    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;
    String baseUrl;

    @BeforeEach
    public void setup() {
        this.baseUrl = "http://localhost:" + randomServerPort + "/games";
    }

    @Test
    @DisplayName("valid search term returns 200")
    public void whenSearchValid_ThenReturn200OK() {
        // Arrange
        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl+"?searchTerm={searchTerm}", String.class, "baldur");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
