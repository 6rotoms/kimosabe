package kimosabe.api.entity;

import kimosabe.api.constants.IgdbConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SearchSummary {
    private long maxNumPages;
    private String searchTerm;
    private long numSearchResults;

    public SearchSummary(long numSearchResults, String searchTerm) {
        this.numSearchResults = numSearchResults;
        this.maxNumPages = (long)Math.ceil(numSearchResults / IgdbConstants.PAGE_SIZE);
        this.searchTerm = searchTerm;
    }
}
