package mz.org.fgh.mentoring.api;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class SuccessResponse implements RestAPIResponse {

    private int status;
    private String message;
    private Object data;

    public static SuccessResponse of(String message, Object data) {
        return SuccessResponse.builder()
                .status(200)
                .message(message)
                .data(data)
                .build();
    }

    public static SuccessResponse messageOnly(String message) {
        return SuccessResponse.builder()
                .status(200)
                .message(message)
                .build();
    }
}
