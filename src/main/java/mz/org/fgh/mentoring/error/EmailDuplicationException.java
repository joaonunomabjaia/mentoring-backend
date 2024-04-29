package mz.org.fgh.mentoring.error;

public class EmailDuplicationException extends RuntimeException{

    public EmailDuplicationException(String message) {
        super(message);
    }
}
