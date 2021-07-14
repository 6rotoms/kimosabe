package kimosabe.api.games;

import kimosabe.api.AbstractBaseIntegrationTest;
import kimosabe.api.entity.ExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchGamesTest extends AbstractBaseIntegrationTest {
    @BeforeEach
    public void setup() {
        super.cleanUpDb();
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

    @Test
    @DisplayName("search with negative pageSize returns 400 with error")
    public void whenSearchPageNumIsNegative_ThenReturn400BadRequest() {
        // Arrange
        // Act
        ResponseEntity<ExceptionResponse> response = restTemplate.getForEntity(baseUrl+"?searchTerm={searchTerm}&pageSize={pageSize}", ExceptionResponse.class, "baldur", -5);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ExceptionResponse body = response.getBody();
        assertThat(body.getMessage()).isEqualTo("request properties did not meet constraints");
        assertThat(body.getErrors().size()).isEqualTo(1);
        assertThat(body.getErrors().get(0)).isEqualTo("pageSize must be greater than 0");
    }

    @Test
    @DisplayName("search with pageSize greater than 100 returns 400 with error")
    public void whenSearchPageNumIsOverMax_ThenReturn400BadRequest() {
        // Arrange
        // Act
        ResponseEntity<ExceptionResponse> response = restTemplate.getForEntity(baseUrl+"?searchTerm={searchTerm}&pageSize={pageSize}", ExceptionResponse.class, "baldur", 101);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ExceptionResponse body = response.getBody();
        assertThat(body.getMessage()).isEqualTo("request properties did not meet constraints");
        assertThat(body.getErrors().size()).isEqualTo(1);
        assertThat(body.getErrors().get(0)).isEqualTo("pageSize must be less than 101");
    }

    @Test
    @DisplayName("valid search summary term returns 200")
    public void whenSearchSummaryValid_ThenReturn200OK() {
        // Arrange
        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl+"/searchInfo?searchTerm={searchTerm}", String.class, "baldur");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("search summary with negative pageSize returns 400")
    public void whenSummaryPageNumIsNegative_ThenReturn400BadRequest() {
        // Arrange
        // Act
        ResponseEntity<ExceptionResponse> response = restTemplate.getForEntity(baseUrl+"/searchInfo?searchTerm={searchTerm}&pageSize={pageSize}", ExceptionResponse.class, "baldur", -5);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ExceptionResponse body = response.getBody();
        assertThat(body.getMessage()).isEqualTo("request properties did not meet constraints");
        assertThat(body.getErrors().size()).isEqualTo(1);
        assertThat(body.getErrors().get(0)).isEqualTo("pageSize must be greater than 0");
    }

    @Test
    @DisplayName("search summary with pageSize greater than 100 returns 400")
    public void whenSummaryPageNumIsOverMax_ThenReturn400BadRequest() {
        // Arrange
        // Act
        ResponseEntity<ExceptionResponse> response = restTemplate.getForEntity(baseUrl+"/searchInfo?searchTerm={searchTerm}&pageSize={pageSize}", ExceptionResponse.class, "baldur", 101);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ExceptionResponse body = response.getBody();
        assertThat(body.getMessage()).isEqualTo("request properties did not meet constraints");
        assertThat(body.getErrors().size()).isEqualTo(1);
        assertThat(body.getErrors().get(0)).isEqualTo("pageSize must be less than 101");
    }
}
