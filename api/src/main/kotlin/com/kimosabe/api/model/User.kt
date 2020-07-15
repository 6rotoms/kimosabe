package com.kimosabe.api.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable
import java.util.*
import java.util.stream.Collectors
import javax.persistence.*


@Table(name = "users")
@Entity
class User (
        @Id @Column(name = "user_id", columnDefinition = "UUID", unique = true) val userId : UUID = UUID.randomUUID(),
        @Column(name = "username", unique = true) private var username : String,
        @Column(name = "password") private var password : String,
        @JsonIgnore @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "users_roles", joinColumns = [JoinColumn(name = "user_id")], inverseJoinColumns = [JoinColumn(name = "role_id")])
        val roles: MutableSet<Role> = mutableSetOf()
) : UserDetails, Serializable {

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
                return roles.stream().map { role: Role -> SimpleGrantedAuthority(role.name!!.name) }.collect(Collectors.toList())
        }

        override fun isEnabled(): Boolean {
                return true
        }

        override fun getUsername(): String {
                return username
        }

        override fun isCredentialsNonExpired(): Boolean {
                return true
        }

        override fun getPassword(): String {
                return password
        }

        fun setPassword(password: String) {
                this.password = password
        }

        override fun isAccountNonExpired(): Boolean {
                return true
        }

        override fun isAccountNonLocked(): Boolean {
                return true
        }
}