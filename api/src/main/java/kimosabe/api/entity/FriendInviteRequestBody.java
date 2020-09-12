package kimosabe.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class FriendInviteRequestBody {
    private String friendUsername;
}
