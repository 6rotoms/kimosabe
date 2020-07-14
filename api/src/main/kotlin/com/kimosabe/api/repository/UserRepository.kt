package com.kimosabe.api.repository

import com.kimosabe.api.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByUsername(username : String) : User?
    fun findByUserId(uuid : UUID) : User?
}