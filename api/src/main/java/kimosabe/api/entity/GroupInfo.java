package kimosabe.api.entity;

import kimosabe.api.model.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class GroupInfo {
    private String groupName;
    private String groupId;
    private String coverUrl;

    public GroupInfo(GameSearchResponse gameInfo, Group groupInfo) {
        this.coverUrl = gameInfo.getCoverUrl();
        this.groupName = groupInfo.getName();
        this.groupId = groupInfo.getGroupId();
    }
}
