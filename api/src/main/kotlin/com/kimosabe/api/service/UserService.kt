package com.kimosabe.api.service

import com.kimosabe.api.model.RoleName
import com.kimosabe.api.model.User
import com.kimosabe.api.repository.RoleRepository
import com.kimosabe.api.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
        val passwordEncoder : PasswordEncoder,
        val roleRepository: RoleRepository,
        val userRepository: UserRepository
) : UserDetailsService {

    @Throws(Error::class)
    override fun loadUserByUsername(username : String) : User {
        val loadedUser = userRepository.findByUsername(username)
        loadedUser?.let { return loadedUser }
        throw Error("User not found with username: $username")
    }

    fun isUsernameAvailable(username : String) : Boolean {
        return userRepository.findByUsername(username) == null
    }

    fun createNewUser(user : User) : User {
        val encodedPassword = passwordEncoder.encode(user.password)
        user.password = encodedPassword
        val userRole = roleRepository.findByName(RoleName.ROLE_USER)
        userRole?.let { user.roles.add(userRole) }
        return userRepository.save(user)
    }
}