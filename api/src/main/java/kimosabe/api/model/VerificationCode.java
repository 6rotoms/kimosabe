package kimosabe.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "verification_codes")
@NoArgsConstructor @Getter @Setter
public class VerificationCode {
    @Id
    @Column(name = "verification_code")
    private String verificationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
}
