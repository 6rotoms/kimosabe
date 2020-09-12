package kimosabe.api.exception;

public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException(String username) {
        super(String.format("Username %s taken", username));
    }
}
