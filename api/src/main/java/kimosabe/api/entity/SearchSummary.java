package kimosabe.api.entity;

import kimosabe.api.constants.IgdbConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SearchSummary {
    private int maxNumPages;
    private String searchTerm;
    private int numSearchResults;

    public SearchSummary(int numSearchResults, String searchTerm) {
        this.numSearchResults = numSearchResults;
        this.maxNumPages = (int)Math.ceil(numSearchResults / IgdbConstants.PAGE_SIZE);
        this.searchTerm = searchTerm;
    }
}
