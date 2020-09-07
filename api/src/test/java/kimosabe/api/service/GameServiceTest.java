package kimosabe.api.service;

import kimosabe.api.entity.GameSearchResult;
import kimosabe.api.entity.SearchSummary;
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
        List<GameSearchResult> mockedResponse = new ArrayList<>();
        mockedResponse.add(new GameSearchResult("id123","baldur's gate", "imgUrl"));
        mockedResponse.add(new GameSearchResult("id234","baldur's gate ii", "imgUrl"));
        when(gameSearchRepository.getSearchResultsPage("baldur", 0)).thenReturn(mockedResponse);

        // Act
        List<GameSearchResult> actualResponse = gameService.searchForGames("baldur", 0);

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
