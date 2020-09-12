package kimosabe.api.exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException() {
        super("The password entered was incorrect");
    }
}
