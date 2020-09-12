package kimosabe.api.service;

import com.redislabs.lettusearch.search.Document;
import kimosabe.api.entity.SearchSummary;
import kimosabe.api.entity.GameSearchResponse;
import kimosabe.api.repository.GameSearchRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    GameSearchRepository gameSearchRepository;
    @InjectMocks
    GameService gameService;

    @AfterEach
    public void cleanUp() {
        Mockito.reset(gameSearchRepository);
    }

    @Test
    public void whenSearchForGameWithString_ThenReturnGameResultArray() {
        // Arrange
        List<GameSearchResponse> mockedResponse = new ArrayList<>();
        Document<String, String> mockDoc1 = new Document<>();
        mockDoc1.setId("id123");
        mockDoc1.put("name", "baldur's gate");
        mockDoc1.put("cover", "img1");
        mockDoc1.put("thumb", "img2");
        mockDoc1.put("summary", "baldurs gate");
        mockedResponse.add(new GameSearchResponse(mockDoc1));
        Document<String, String> mockDoc2 = new Document<>();
        mockDoc2.setId("id124");
        mockDoc2.put("name", "baldur's gate ii");
        mockDoc2.put("cover", "img11");
        mockDoc2.put("thumb", "img22");
        mockDoc2.put("summary", "baldurs gate 2");
        mockedResponse.add(new GameSearchResponse(mockDoc2));
        when(gameSearchRepository.getSearchResultsPage("baldur", 0)).thenReturn(mockedResponse);

        // Act
        List<GameSearchResponse> actualResponse = gameService.searchForGames("baldur", 0);

        // Assert
        assertThat(actualResponse).isEqualTo(mockedResponse);
    }
    @Test
    public void whenGettingSearchSummary_ThenReturnSearchSummary() {
        // Arrange
        SearchSummary mockedResponse = new SearchSummary(500, "searchTerm");
        when(gameSearchRepository.getSearchSummary("searchTerm")).thenReturn(mockedResponse);

        // Act
        SearchSummary actualResponse = gameService.getSearchSummary("searchTerm");

        // Assert
        assertThat(actualResponse).isEqualTo(mockedResponse);
    }
}
