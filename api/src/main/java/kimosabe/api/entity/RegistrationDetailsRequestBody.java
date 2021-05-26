package kimosabe.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @NoArgsConstructor
public class RegistrationDetailsRequestBody {
    @NotEmpty(message = "username field is required")
    private String username;
    @NotEmpty(message = "email field is required")
    private String email;
    @NotEmpty(message = "password field is required")
    private String password;
}
