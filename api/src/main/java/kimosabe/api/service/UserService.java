package kimosabe.api.service;

import kimosabe.api.entity.UserProfileInfo;
import kimosabe.api.exception.BadRequestException;
import kimosabe.api.entity.FriendAnswerRequestBody;
import kimosabe.api.entity.FriendInviteRequestBody;
import kimosabe.api.exception.MissingDatabaseEntryException;
import kimosabe.api.exception.MissingRoleException;
import kimosabe.api.exception.EntityExistsException;
import kimosabe.api.model.*;
import kimosabe.api.repository.RoleRepository;
import kimosabe.api.repository.UserRelationshipRepository;
import kimosabe.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRelationshipRepository relationshipRepository;

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
            throw new EntityExistsException("User with username " + user.getUsername());
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
        if (password.length() < 3) {
            throw new BadRequestException("Password must be longer than 3 characters");
        }
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public void createFriendRequest(String username, FriendInviteRequestBody friendInvite) {
        User requester = getUserByUsername(username);
        User target = getUserByUsername(friendInvite.getFriendUsername());
        relationshipRepository.save(new UserRelationship(requester, target, RelationshipStatus.PENDING));
    }

    public void acceptFriendRequest(String username, FriendAnswerRequestBody friendAnswer) {
        User requester = getUserByUsername(friendAnswer.getFrom());
        User target = getUserByUsername(username);
        Optional<UserRelationship> relationship = relationshipRepository
                .findById(new UserRelationshipId(requester, target));
        if (relationship.isEmpty()) {
            throw new MissingDatabaseEntryException("Friend Request");
        }
        UserRelationship actualRelationship = relationship.get();
        if (friendAnswer.isAccept()) {
            actualRelationship.setRelationshipStatus(RelationshipStatus.ACCEPTED);
            relationshipRepository.save(actualRelationship);
        } else {
            relationshipRepository.delete(actualRelationship);
        }
    }

    public void deleteFriend(String username, String friendName) {
        User user = getUserByUsername(username);
        User friend = getUserByUsername(friendName);
        Optional<UserRelationship> relationship =
                relationshipRepository.findByIdAndRelationshipStatus(new UserRelationshipId(user, friend),
                        RelationshipStatus.ACCEPTED);
        if (relationship.isEmpty()) {
            relationship = relationshipRepository.findByIdAndRelationshipStatus(new UserRelationshipId(friend, user),
                    RelationshipStatus.ACCEPTED);
        }
        if (relationship.isEmpty()) {
            throw new MissingDatabaseEntryException("Friend Relationship");
        }
        relationshipRepository.delete(relationship.get());
    }

    public void updateLoginTime(String username) {
        User user = getUserByUsername(username);
        user.setLastLogin(Instant.now());
        userRepository.save(user);
    }

    public void updateUserInfo(String username, UserProfileInfo profileInfo) {
        User user = getUserByUsername(username);
        if (profileInfo.getAge() != null) {
            user.setAge(profileInfo.getAge());
        }
        if (profileInfo.getBiography() != null && !profileInfo.getBiography().isBlank()) {
            user.setBiography(profileInfo.getBiography());
        }
        if (profileInfo.getGender() != null && !profileInfo.getGender().isBlank()) {
            user.setGender(profileInfo.getGender());
        }
        if (profileInfo.getLocation() != null && !profileInfo.getLocation().isBlank()) {
            user.setLocation(profileInfo.getLocation());
        }
        userRepository.save(user);
    }

    public void blockUser(String username, String targetName){
        User user = getUserByUsername(username);
        User targetUser = getUserByUsername(targetName);

        Optional<UserRelationship> relationship =
                relationshipRepository.findById(new UserRelationshipId(user, targetUser));
        if (relationship.isEmpty()){
            relationship =
                    relationshipRepository.findById(new UserRelationshipId(targetUser, user));
        }

        if(!relationship.isEmpty()){
            relationshipRepository.delete(relationship.get());
        }

        relationshipRepository.save(new UserRelationship(user, targetUser, RelationshipStatus.BLOCKED));
    }

    public void unblockUser(String username, String blockedName){
        User user = getUserByUsername(username);
        User blockedUser = getUserByUsername(blockedName);

        Optional<UserRelationship> relationship =
                relationshipRepository.findByIdAndRelationshipStatus(new UserRelationshipId(user, blockedUser),
                        RelationshipStatus.BLOCKED);
        if (relationship.isEmpty()){
            throw new MissingDatabaseEntryException("Blocked Relationship");
        }

        relationshipRepository.delete(relationship.get());
    }
}
