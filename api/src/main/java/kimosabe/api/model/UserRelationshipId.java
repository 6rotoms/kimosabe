package kimosabe.api.model;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
public class UserRelationshipId implements Serializable {
    @Column(name="requester_id")
    private UUID requesterId;
    @Column(name="target_id")
    private UUID targetId;

    public UserRelationshipId(UUID requester, UUID target) {
        this.requesterId = requester;
        this.targetId = target;
    }
    public UserRelationshipId(User a, User b) {
        this(a.getId(), b.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRelationshipId that = (UserRelationshipId) o;
        return Objects.equals(requesterId, that.requesterId) &&
                Objects.equals(targetId, that.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requesterId, targetId);
    }
}