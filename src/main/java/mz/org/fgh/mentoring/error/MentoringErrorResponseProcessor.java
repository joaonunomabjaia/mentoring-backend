package mz.org.fgh.mentoring.error;

import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import jakarta.inject.Singleton;
import lombok.NonNull;

@Singleton
public class MentoringErrorResponseProcessor implements ErrorResponseProcessor<MentoringAPIError> {
    @Override
    public MutableHttpResponse<MentoringAPIError> processResponse(@NonNull ErrorContext errorContext, @NonNull MutableHttpResponse<?> response) {
       MentoringAPIError mentoringAPIError;

        if (!errorContext.hasErrors()) {
            mentoringAPIError = new MentoringAPIError(response.getStatus().getCode(), response.getStatus().name(), "Ocorreu um erro ao tentar executar a operação.");
        } else {
            mentoringAPIError = new MentoringAPIError(response.getStatus().getCode(), response.getStatus().name(), errorContext.getErrors().get(0).getMessage());

        }
        return response.body(mentoringAPIError).contentType(MediaType.APPLICATION_JSON);
    }
}
