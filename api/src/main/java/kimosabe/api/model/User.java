package kimosabe.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Table(name = "users")
@Entity
@Getter @Setter @NoArgsConstructor
public class User implements UserDetails {
    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "requester", fetch = FetchType.LAZY)
    private Set<UserRelationship> requestedRelationships;

    @OneToMany(mappedBy = "target", fetch = FetchType.LAZY)
    private Set<UserRelationship> targetRelationships;

    @Column(name = "age")
    private Integer age;

    @Column(name="biography", columnDefinition = "TEXT")
    private String biography;

    @Column(name = "gender")
    private String gender;

    @Column(name = "last_login")
    private Date lastLogin;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.id = UUID.randomUUID();
    }

    public void addRole(Role newRole) {
        roles.add(newRole);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().toString())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private Set<User> getUsersOnRelationshipStatus(RelationshipStatus status) {
        return Stream.concat(
                requestedRelationships.stream()
                        .filter(r -> r.getRelationshipStatus() == status)
                        .map(UserRelationship::getTarget),
                targetRelationships.stream()
                        .filter(r -> r.getRelationshipStatus() == status)
                        .map(UserRelationship::getRequester))
                .collect(Collectors.toSet());
    }

    public Set<User> getFriends() {
        return getUsersOnRelationshipStatus(RelationshipStatus.ACCEPTED);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User other = (User) obj;
            return this.username.equals(other.username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}