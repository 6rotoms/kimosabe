package kimosabe.api.service;

import kimosabe.api.entity.GameSearchResponse;
import kimosabe.api.entity.SearchSummary;
import kimosabe.api.repository.GameSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameSearchRepository gameSearchRepository;

    @Autowired
    public GameService(GameSearchRepository gameSearchRepository) {
        this.gameSearchRepository = gameSearchRepository;
    }

    public List<GameSearchResponse> searchForGames(String searchTerm, int pageNumber) {
        return gameSearchRepository.getSearchResultsPage(searchTerm, pageNumber);
    }

    public SearchSummary getSearchSummary(String searchTerm) {
        return gameSearchRepository.getSearchSummary(searchTerm);
    }
}
