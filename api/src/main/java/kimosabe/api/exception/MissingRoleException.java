package kimosabe.api.exception;

import kimosabe.api.model.RoleName;

public class MissingRoleException extends RuntimeException {
    public MissingRoleException(RoleName field) {
        super(String.format("Role %s is missing", field.name()));
    }
}
