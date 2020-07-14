package com.kimosabe.api.service

import com.kimosabe.api.model.Role
import com.kimosabe.api.model.RoleName
import com.kimosabe.api.model.User
import com.kimosabe.api.repository.RoleRepository
import com.kimosabe.api.repository.UserRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.mockk.verifyAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import java.util.*

@ExtendWith(MockKExtension::class)
class UserServiceTest {
  @MockK
  private lateinit var userRepository: UserRepository
  @MockK
  private lateinit var roleRepository: RoleRepository
  @MockK
  private lateinit var passwordEncoder: PasswordEncoder

  @InjectMockKs
  private lateinit var userService: UserService

  @AfterEach
  fun cleanup() {
    clearAllMocks()
  }

  @Test
  fun whenCreateNewUserCalled_ThenNewUserShouldBeAdded() {
    // Arrange
    val user : User = User(UUID.randomUUID(), "username", "password")
    every { passwordEncoder.encode("password") } returns "encodedpassword"
    every { roleRepository.findByName(RoleName.ROLE_USER) } returns Role()
    every { userRepository.save(user) } returns user

    // Act
    userService.createNewUser(user)

    // Assert
    verifyAll {
      passwordEncoder.encode("password")
      roleRepository.findByName(RoleName.ROLE_USER)
    }
    verify(exactly=1) { userRepository.save(user) }
  }

  @Test
  fun whenCreateNewUserCalled_ThenPasswordShouldBeEncrypted() {
    // Arrange
    val user : User = User(UUID.randomUUID(), "username", "password")
    every { passwordEncoder.encode("password") } returns "encodedpassword"
    every { roleRepository.findByName(RoleName.ROLE_USER) } returns Role()
    every { userRepository.save(user) } returns user

    // Act
    val response = userService.createNewUser(user)

    // Assert
    assertThat(response.password).isEqualTo("encodedpassword")
  }

  @Test
  fun whenLoadUserByUsernameCalled_AndUsernameIsValid_ThenReturnUser() {
    // Arrange
    val user : User = User(UUID.randomUUID(), "username", "password")
    every { userRepository.findByUsername("username") } returns user

    // Act
    val response = userService.loadUserByUsername("username")

    // Assert
    verifyAll {
      userRepository.findByUsername("username")
    }
    assertThat(response.password).isEqualTo("password")
  }

  @Test
  fun whenLoadUserByUsernameCalled_AndUsernameIsInalid_ThenErrorShouldBeThrown() {
    // Arrange
    every { userRepository.findByUsername("username") } returns null

    // Assert
    assertThrows<Error> { userService.loadUserByUsername("username") }
    verifyAll {
      userRepository.findByUsername("username")
    }
  }
}