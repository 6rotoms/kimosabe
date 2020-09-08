package kimosabe.api.repository;

import com.redislabs.lettusearch.StatefulRediSearchConnection;
import com.redislabs.lettusearch.search.Limit;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import kimosabe.api.constants.IgdbConstants;
import kimosabe.api.entity.GameSearchResult;
import kimosabe.api.entity.SearchSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameSearchRepository {
    private StatefulRediSearchConnection<String, String> conn;

    @Autowired
    public GameSearchRepository(StatefulRediSearchConnection<String, String> conn) {
        this.conn = conn;
    }

    public SearchSummary getSearchSummary(String searchTerm) {
        long searchCount = conn.sync().search("games", searchTerm).getCount();
        return new SearchSummary(searchCount, searchTerm);
    }

    public List<GameSearchResult> getSearchResultsPage(String searchTerm, int pageNumber) {
        SearchOptions.SearchOptionsBuilder options = SearchOptions.builder()
                .limit(Limit.builder().num(IgdbConstants.PAGE_SIZE)
                        .offset(pageNumber * IgdbConstants.PAGE_SIZE).build());
        SearchResults<String, String> results = conn.sync().search("games", searchTerm, options.build());
        return results.stream().map((doc) ->
                new GameSearchResult(doc.getId(), doc.get("name"), doc.get("cover"))).collect(Collectors.toList());
    }
}
