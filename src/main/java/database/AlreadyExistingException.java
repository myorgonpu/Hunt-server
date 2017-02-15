package main.java.database;


public class AlreadyExistingException extends Exception {

    public AlreadyExistingException() {
    }

    public AlreadyExistingException(String message) {
        super(message);
    }
}
