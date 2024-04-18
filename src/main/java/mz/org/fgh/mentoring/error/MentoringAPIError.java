package mz.org.fgh.mentoring.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.api.RestAPIResponse;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentoringAPIError implements RestAPIResponse {
    private int status;
    private String error;
    private String message;

}
