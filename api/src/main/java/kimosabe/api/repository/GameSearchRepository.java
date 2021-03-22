package kimosabe.api.repository;

import com.redislabs.lettusearch.StatefulRediSearchConnection;
import com.redislabs.lettusearch.SearchOptions;
import com.redislabs.lettusearch.SearchResults;
import kimosabe.api.constants.IgdbConstants;
import kimosabe.api.entity.SearchSummary;
import kimosabe.api.entity.GameSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GameSearchRepository {
    private final StatefulRediSearchConnection<String, String> conn;

    @Autowired
    public GameSearchRepository(StatefulRediSearchConnection<String, String> conn) {
        this.conn = conn;
    }

    public SearchSummary getSearchSummary(String searchTerm) {
        long searchCount = conn.sync().search("games", searchTerm).getCount();
        return new SearchSummary(searchCount, searchTerm);
    }

    public List<GameSearchResponse> getSearchResultsPage(String searchTerm, int pageNumber) {
        SearchOptions.SearchOptionsBuilder options = SearchOptions.builder()
                .limit(SearchOptions.Limit.builder().num(IgdbConstants.PAGE_SIZE)
                        .offset(pageNumber * IgdbConstants.PAGE_SIZE).build());
        SearchResults<String, String> results = conn.sync().search("games", searchTerm, options.build());
        return results.stream().map(GameSearchResponse::new).collect(Collectors.toList());
    }

    public Map<String, String> getGameById(String gameId) {
        return conn.sync().hgetall(gameId);
    }
}
