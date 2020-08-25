package kimosabe.api.service;
import kimosabe.api.entity.GameSearchResult;
import kimosabe.api.repository.GameSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private GameSearchRepository gameSearchRepository;

    @Autowired
    public GameService(GameSearchRepository gameSearchRepository) {
        this.gameSearchRepository = gameSearchRepository;
    }

    public List<GameSearchResult> searchForGames(String searchTerm) {
        return this.gameSearchRepository.getSearchResults(searchTerm);
    }
}
