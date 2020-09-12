package kimosabe.api.exception;

public class MissingDatabaseEntryException extends RuntimeException {
    public MissingDatabaseEntryException(String field) {
        super(String.format("%s is missing from database", field));
    }
}
