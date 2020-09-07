package kimosabe.api.repository;

import com.redislabs.lettusearch.StatefulRediSearchConnection;
import com.redislabs.lettusearch.search.Limit;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import kimosabe.api.entity.GameSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameSearchRepository {
    private StatefulRediSearchConnection<String, String> conn;

    @Value("${spring.igdbpdt.pageSize}")
    private Integer pageSize;

    @Autowired
    public GameSearchRepository(StatefulRediSearchConnection<String, String> conn) {
        this.conn = conn;
    }

    public List<GameSearchResult> getSearchResults(String searchTerm, int pageNumber) {
        SearchOptions.SearchOptionsBuilder options = SearchOptions.builder()
                .limit(Limit.builder().num(pageSize).offset(pageNumber * pageSize).build());
        SearchResults<String, String> results = conn.sync().search("games", searchTerm, options.build());
        return results.stream().map((doc) ->
                new GameSearchResult(doc.getId(), doc.get("name"), doc.get("cover"))).collect(Collectors.toList());
    }
}
