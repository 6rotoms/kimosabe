package kimosabe.api.exceptions;

public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException() {
        super("The password entered was incorrect");
    }
}
