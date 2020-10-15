package kimosabe.api.exception;

public class EntityExistsException extends RuntimeException {
    public EntityExistsException(String entity) {
        super(String.format("%s already exists", entity));
    }
}
