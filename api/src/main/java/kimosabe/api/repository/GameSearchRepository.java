package kimosabe.api.repository;

import com.redislabs.lettusearch.SearchOptions;
import com.redislabs.lettusearch.SearchResults;
import com.redislabs.lettusearch.StatefulRediSearchConnection;
import kimosabe.api.entity.GameSearchResponse;
import kimosabe.api.entity.SearchSummary;
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

    public SearchSummary getSearchSummary(String searchTerm, int pageSize) {
        long searchCount = conn.sync().search("games", searchTerm).getCount();
        return new SearchSummary(searchCount, searchTerm, pageSize);
    }

    public List<GameSearchResponse> getSearchResultsPage(String searchTerm, int pageNumber, int pageSize) {
        SearchOptions.SearchOptionsBuilder options = SearchOptions.builder()
                .limit(SearchOptions.Limit.builder().num(pageSize)
                        .offset(pageNumber * pageSize).build());
        SearchResults<String, String> results = conn.sync().search("games", searchTerm, options.build());
        return results.stream().map(GameSearchResponse::new).collect(Collectors.toList());
    }

    public Map<String, String> getGameById(String gameId) {
        return conn.sync().hgetall(gameId);
    }
}
