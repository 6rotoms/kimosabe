package kimosabe.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
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

    @ManyToMany(mappedBy = "members")
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "requester")
    private Set<UserRelationship> requestedRelationships;

    @OneToMany(mappedBy = "target")
    private Set<UserRelationship> targetRelationships;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.id = UUID.randomUUID();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().toString())).collect(Collectors.toList());
    }

    public void addRole(Role newRole) {
        roles.add(newRole);
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
}