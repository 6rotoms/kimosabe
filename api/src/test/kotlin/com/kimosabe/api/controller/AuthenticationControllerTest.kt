package com.kimosabe.api.controller

import com.kimosabe.api.model.User
import com.kimosabe.api.service.UserService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.authentication.AuthenticationManager
import java.util.*

@ExtendWith(MockKExtension::class)
class AuthenticationControllerTest {
  @MockK
  lateinit var userService: UserService

  @MockK
  lateinit var authManager: AuthenticationManager

  @InjectMockKs
  lateinit var authenticationController: AuthenticationController

  @Test
  fun whenCreateUserCalled_AndUsernameExists_ThenUserShouldBeCreated() {
    // Arrange
    every { userService.isUsernameAvailable("username") } returns true
    val user = User(UUID.randomUUID(), "username", "password")
    every { userService.createNewUser(user) } returns user

    // Act
    authenticationController.createUser(user)

    // Assert
    verify { userService.createNewUser(user) }
  }
}