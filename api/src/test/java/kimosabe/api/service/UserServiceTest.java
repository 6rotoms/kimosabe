package kimosabe.api.service;

import kimosabe.api.exceptions.IncorrectPasswordException;
import kimosabe.api.exceptions.MissingDatabaseEntryException;
import kimosabe.api.exceptions.UsernameTakenException;
import kimosabe.api.model.Role;
import kimosabe.api.model.RoleName;
import kimosabe.api.model.User;
import kimosabe.api.repository.RoleRepository;
import kimosabe.api.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RoleRepository roleRepository;
    @InjectMocks
    UserService userService;

    private User user;

    @BeforeEach
    public void setUpHappyPath() {
        // Arrange
        user = new User("username", "password");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(new Role()));
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(userRepository, passwordEncoder, roleRepository);
    }

    @Test
    public void whenCreateNewUserCalledValid_ThenNewUserShouldBeSaved() throws Exception {
        // Act
        User u = userService.createNewUser(user);

        // Assert
        verify(userRepository, times(1)).save(user);
        assertThat(u).isEqualTo(user);
    }

    @Test
    public void whenCreateNewUserCalledValid_ThenPasswordShouldBeEncrypted() throws Exception {
        // Act
        User u = userService.createNewUser(user);

        // Assert
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    public void whenCreateNewUserCalledUsernameTaken_ThenExceptionThrown() {
        // Arrange
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(new User("username", "password")));

        // Act
        // Assert
        Assertions.assertThrows(UsernameTakenException.class, () -> userService.createNewUser(user));
    }

    @Test
    public void whenCreateNewUserCalledRoleMissing_ThenExceptionThrown() {
        // Arrange
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.empty());

        // Act
        // Assert
        Assertions.assertThrows(MissingDatabaseEntryException.class, () -> userService.createNewUser(user));
    }

    @Test
    public void whenLoadUserByUsernameCalledInvalid_ThenThrowException() {
        // Act
        // Assert
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("username"));
    }

    @Test
    public void whenCheckPasswordCalledValid_ThenReturnTrue(){
        // Arrange
        when(passwordEncoder.matches("password", "password")).thenReturn(true);

        // Act
        // Assert
        Assertions.assertEquals(true, userService.checkPassword(user, "password"));
    }

    @Test
    public void whenCheckPasswordCalledIncorrectOldPassword_ThenReturnFalse(){
        // Assert
        Assertions.assertEquals(false, userService.checkPassword(user, "wrongPassword"));
    }

    @Test
    public void whenChangePasswordCalled_ThenPasswordChanged(){
        // Arrange
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        // Act
        userService.changePassword(user, "newPassword");

        // Assert
        assertThat(user.getPassword()).isEqualTo("encodedNewPassword");
    }
}
