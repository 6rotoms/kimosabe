package kimosabe.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "roles")
@Entity
@NoArgsConstructor @Getter @Setter
public class Role implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;

    public Role(RoleName name) {
        this.name = name;
    }
}
