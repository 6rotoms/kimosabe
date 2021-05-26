package kimosabe.api.service;

import kimosabe.api.entity.FriendAnswerRequestBody;
import kimosabe.api.entity.FriendInviteRequestBody;
import kimosabe.api.entity.UserProfileInfo;
import kimosabe.api.exception.BadRequestException;
import kimosabe.api.exception.EntityExistsException;
import kimosabe.api.exception.MissingDatabaseEntryException;
import kimosabe.api.exception.MissingRoleException;
import kimosabe.api.model.*;
import kimosabe.api.repository.RoleRepository;
import kimosabe.api.repository.UserRelationshipRepository;
import kimosabe.api.repository.UserRepository;
import kimosabe.api.repository.VerificationCodeRepository;
import kimosabe.api.utils.StringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final UserRelationshipRepository relationshipRepository;
    private final JavaMailSender emailSender;
    private final StringGenerator stringGenerator;

    @Autowired
    public UserService(
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            UserRepository userRepository,
            VerificationCodeRepository verificationCodeRepository,
            UserRelationshipRepository relationshipRepository,
            JavaMailSender emailSender,
            StringGenerator stringGenerator
    ) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.relationshipRepository = relationshipRepository;
        this.emailSender = emailSender;
        this.stringGenerator = stringGenerator;
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

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EntityExistsException("User with email " + user.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Optional<Role> userRole = roleRepository.findByName(RoleName.ROLE_USER);
        if (userRole.isEmpty()) {
            throw new MissingRoleException(RoleName.ROLE_USER);
        }
        user.addRole(userRole.get());
        User response = userRepository.save(user);
        sendVerificationEmail(response);
        return response;
    }

    public void sendVerificationEmail(User user) {
        if (user.hasRole(RoleName.ROLE_VERIFIED)) {
            return;
        }
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            String token = stringGenerator.generateAlphaNumeric(8);
            VerificationCode code = new VerificationCode();
            helper.setText(String.format("<span>Click <a href=\"https://kimosabe.cyou/verify?token=%s\">here</a> to verify your account.</span>", token), true); // Use this or above line.
            helper.setTo(user.getEmail());
            helper.setSubject("Welcome to kimosabe!");
            helper.setFrom("verify@kimosabe.cyou");
            emailSender.send(message);
            code.setVerificationCode(token);
            code.setUser(user);
            verificationCodeRepository.save(code);
        } catch(MessagingException ex) {
            throw new BadRequestException("email send failed");
        }
    }

    public void verifyUser(String token) {
        Optional<VerificationCode> result = verificationCodeRepository.findById(token);
        if (result.isEmpty()) {
            throw new MissingDatabaseEntryException("verification code");
        }
        VerificationCode code = result.get();
        if (code.getUser().hasRole(RoleName.ROLE_VERIFIED)) {
            return;
        }
        User u = code.getUser();
        Optional<Role> verifiedRole = roleRepository.findByName(RoleName.ROLE_VERIFIED);
        if (verifiedRole.isEmpty()) {
            throw new MissingRoleException(RoleName.ROLE_VERIFIED);
        }
        u.addRole(verifiedRole.get());
        userRepository.save(u);
        verificationCodeRepository.delete(code);
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

        if (user.equals(targetUser)){
            throw new BadRequestException("Blocking yourself is forbidden");
        }

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
