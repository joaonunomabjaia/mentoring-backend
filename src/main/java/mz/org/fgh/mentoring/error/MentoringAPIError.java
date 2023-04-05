package mz.org.fgh.mentoring.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.api.RestAPIResponse;

@Data
public class MentoringAPIError implements RestAPIResponse {
    private int status;
    private String error;
    private String message;

    public MentoringAPIError() {
    }

    public MentoringAPIError(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
