package mz.org.fgh.mentoring.error;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Produces
@Singleton
public class RecordInUseExceptionHandler implements ExceptionHandler<RecordInUseException, HttpResponse<MentoringAPIError>> {

    private static final Logger LOG = LoggerFactory.getLogger(RecordInUseExceptionHandler.class);

    @Override
    public HttpResponse<MentoringAPIError> handle(HttpRequest request, RecordInUseException exception) {
        LOG.warn("RecordInUseException: {}", exception.getMessage());

        return HttpResponse.status(HttpStatus.CONFLICT).body(
                MentoringAPIError.builder()
                        .status(HttpStatus.CONFLICT.getCode())
                        .message(exception.getMessage())   // mensagem amigável
                        .error(exception.getMessage())     // mensagem técnica (mesma aqui)
                        .build()
        );
    }
}
