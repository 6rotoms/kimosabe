package kimosabe.api.service;

import kimosabe.api.exception.BadRequestException;
import kimosabe.api.entity.FriendAnswerRequestBody;
import kimosabe.api.entity.FriendInviteRequestBody;
import kimosabe.api.exception.MissingDatabaseEntryException;
import kimosabe.api.exception.MissingRoleException;
import kimosabe.api.exception.UsernameTakenException;
import kimosabe.api.model.*;
import kimosabe.api.repository.RoleRepository;
import kimosabe.api.repository.UserRelationshipRepository;
import kimosabe.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private UserRelationshipRepository relationshipRepository;

    @Autowired
    public UserService(
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            UserRepository userRepository,
            UserRelationshipRepository relationshipRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.relationshipRepository = relationshipRepository;
    }

    public User getUserByUsername(String username) {
        Optional<User> loadedUser = userRepository.findByUsername(username) ;
        if (loadedUser.isEmpty()) {
            throw new MissingDatabaseEntryException(String.format("User with username %s", username));
        }
        return loadedUser.get();
    }

    public User createNewUser(User user) {
        if (user.getUsername().length() < 3 || user.getPassword().length() < 3) {
            throw new BadRequestException("Username and password must be longer than 3 characters");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameTakenException(user.getUsername());
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Optional<Role> userRole = roleRepository.findByName(RoleName.ROLE_USER);
        if (userRole.isEmpty()) {
            throw new MissingRoleException(RoleName.ROLE_USER);
        }
        user.addRole(userRole.get());
        return userRepository.save(user);
    }

    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public void changePassword(User user, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public void createFriendRequest(String username, FriendInviteRequestBody friendInvite) {
        User requester = this.getUserByUsername(username);
        User target = this.getUserByUsername(friendInvite.getFriendUsername());
        relationshipRepository.save(new UserRelationship(requester, target, RelationshipStatus.PENDING));
    }

    public void acceptFriendRequest(String username, FriendAnswerRequestBody friendAnswer) {
        User requester = getUserByUsername(friendAnswer.getFrom());
        User target = getUserByUsername(username);
        Optional<UserRelationship> relationship = relationshipRepository
                .findById(new UserRelationshipId(requester, target));
        if (relationship.isEmpty()) {
            throw new MissingDatabaseEntryException("Relationship");
        }
        UserRelationship actualRelationship = relationship.get();
        if (friendAnswer.isAccept()) {
            actualRelationship.setRelationshipStatus(RelationshipStatus.ACCEPTED);
            relationshipRepository.save(actualRelationship);
        } else {
            relationshipRepository.delete(actualRelationship);
        }
    }
}
