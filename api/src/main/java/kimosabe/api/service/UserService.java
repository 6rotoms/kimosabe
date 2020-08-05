package kimosabe.api.service;

import kimosabe.api.exceptions.MissingDatabaseEntryException;
import kimosabe.api.exceptions.UsernameTakenException;
import kimosabe.api.model.Role;
import kimosabe.api.model.RoleName;
import kimosabe.api.model.User;
import kimosabe.api.repository.RoleRepository;
import kimosabe.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> loadedUser = userRepository.findByUsername(username) ;
        if (loadedUser.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with username %s not found", username));
        }
        return loadedUser.get();
    }

    public User createNewUser(User user) throws Exception {
        if (!userRepository.findByUsername(user.getUsername()).isEmpty()) {
            throw new UsernameTakenException(user.getUsername());
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Optional<Role> userRole = roleRepository.findByName(RoleName.ROLE_USER);
        if (userRole.isEmpty()) {
            throw new MissingDatabaseEntryException("User Role");
        }
        user.addRole(userRole.get());
        return userRepository.save(user);
    }

    public boolean checkPassword(User user, String password){
        String encodedPassword = passwordEncoder.encode(password);
        return encodedPassword.equals(user.getPassword());
    }

    public void changePassword(User user, String password){
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
    }
}
