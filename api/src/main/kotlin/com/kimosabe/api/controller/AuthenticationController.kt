package com.kimosabe.api.controller

import com.kimosabe.api.model.User
import com.kimosabe.api.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession


@RestController
@RequestMapping("auth")
class AuthenticationController(
        val authManager : AuthenticationManager,
        val userService: UserService
) {
    @PostMapping("/login")
    fun authenticateUser(@RequestHeader username : String, @RequestHeader password : String, session : HttpSession) : ResponseEntity<Any> {
        val auth = authManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        val securityContext = SecurityContextHolder.getContext()
        securityContext.authentication = auth
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext)
        return ResponseEntity(auth.principal, HttpStatus.OK)
    }

    @PostMapping("/register")
    fun createUser(@RequestBody user : User) : ResponseEntity<Any> {
        if (!userService.isUsernameAvailable(user.username)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        userService.createNewUser(user)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/logout")
    fun destroySession(session: HttpSession) {
        session.invalidate()
    }
}