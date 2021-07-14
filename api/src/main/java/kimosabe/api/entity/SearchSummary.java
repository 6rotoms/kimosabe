package kimosabe.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SearchSummary {
    private long maxNumPages;
    private String searchTerm;
    private long numSearchResults;

    public SearchSummary(long numSearchResults, String searchTerm, int pageSize) {
        this.numSearchResults = numSearchResults;
        this.maxNumPages = (long)Math.ceil(numSearchResults / pageSize);
        this.searchTerm = searchTerm;
    }
}
