package com.kimosabe.api.repository

import com.kimosabe.api.model.Role
import com.kimosabe.api.model.RoleName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: JpaRepository<Role, Long> {
    fun findByName(roleName : RoleName) : Role?
}