package kimosabe.api.exceptions;

public class MissingDatabaseEntryException extends Exception{
    public MissingDatabaseEntryException(String field) {
        super(String.format("%s is missing from database", field));
    }
}
