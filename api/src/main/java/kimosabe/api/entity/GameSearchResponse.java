package kimosabe.api.entity;

import com.redislabs.lettusearch.search.Document;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter @Setter @NoArgsConstructor
public class GameSearchResponse {
    private String name;
    private String coverUrl;
    private String thumbUrl;
    private String summary;
    private String id;

    public GameSearchResponse(Document<String, String> doc) {
        this.id = doc.getId();
        this.name = doc.get("name");
        this.coverUrl = doc.get("cover");
        this.thumbUrl = doc.get("thumb");
        this.summary = doc.get("summary");
    }

    public GameSearchResponse(String gameId, Map<String, String> map) {
        this.id = gameId;
        this.name = map.getOrDefault("name", "");
        this.coverUrl = map.getOrDefault("cover", "");
        this.thumbUrl = map.getOrDefault("thumb", "");
        this.summary = map.getOrDefault("summary", "");
    }
}
