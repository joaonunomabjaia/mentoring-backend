package mz.org.fgh.mentoring.api;

public class RestAPIResponseImpl implements RestAPIResponse {
    private final int status;
    private final String message;

    public RestAPIResponseImpl(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
