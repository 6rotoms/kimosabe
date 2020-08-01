package kimosabe.api.exceptions;

public class UsernameTakenException extends Exception {
    public UsernameTakenException(String username) {
        super(String.format("Username %s taken", username));
    }
}
