package kimosabe.api.repository;

import kimosabe.api.model.RelationshipStatus;
import kimosabe.api.model.UserRelationship;
import kimosabe.api.model.UserRelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRelationshipRepository extends JpaRepository<UserRelationship, UserRelationshipId> {
    public Optional<UserRelationship> findByIdAndRelationshipStatus(UserRelationshipId id, RelationshipStatus relationshipStatus);
}
