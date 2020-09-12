package kimosabe.api.model;

import kimosabe.api.entity.GameSearchResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Table(name = "groups")
@Entity
@NoArgsConstructor @Getter @Setter
public class Group implements Serializable {
    @Id
    @Column(name="id", unique = true)
    private String groupId;

    @Column(name="name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_groups", joinColumns = @JoinColumn(name = "groups_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<User> members = new HashSet<>();

    public Group(GameSearchResponse game) {
        this.groupId = game.getId();
        this.name = game.getName();
    }

    public void addUser(User user) {
        members.add(user);
    }
}
