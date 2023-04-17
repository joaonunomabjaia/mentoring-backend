package mz.org.fgh.mentoring.error;

public class MentoringBusinessException extends RuntimeException {

    public MentoringBusinessException() {
        super();
    }

    public MentoringBusinessException(String message) {
        super(message);
    }

    public MentoringBusinessException(String message, Throwable cause){
        super(message, cause);
    }
}
