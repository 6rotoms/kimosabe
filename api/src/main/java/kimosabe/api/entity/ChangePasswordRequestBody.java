package kimosabe.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ChangePasswordRequestBody {
    private String oldPassword;
    private String newPassword;
}
