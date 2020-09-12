package kimosabe.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "user_relationships")
@Entity
@NoArgsConstructor @Getter @Setter
public class UserRelationship {
    @EmbeddedId
    private UserRelationshipId id;

    @ManyToOne
    @MapsId("requesterId")
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne
    @MapsId("targetId")
    @JoinColumn(name = "target_id")
    private User target;

    @Enumerated(EnumType.STRING)
    private RelationshipStatus relationshipStatus;

    public UserRelationship(User requester, User target, RelationshipStatus relationshipStatus) {
        this.id = new UserRelationshipId(requester, target);
        this.requester = requester;
        this.target = target;
        this.relationshipStatus = relationshipStatus;
    }
}
