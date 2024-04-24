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
            mentoringAPIError = MentoringAPIError.builder().status(response.getStatus().getCode())
                    .error(response.getStatus().name())
                    .message("Não foi possivel terminar a operação por ocorrencia de um erro.").build();

        } else {
            mentoringAPIError = MentoringAPIError.builder().status(response.getStatus().getCode())
                    .error(response.getStatus().name())
                    .message(errorContext.getErrors().get(0).getMessage()).build();

        }
        return response.body(mentoringAPIError).contentType(MediaType.APPLICATION_JSON);
    }
}
