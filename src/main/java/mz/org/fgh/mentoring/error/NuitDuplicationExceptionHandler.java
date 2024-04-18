package mz.org.fgh.mentoring.error;


import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;


@Produces
@Singleton
@Requires(classes = {NuitDuplicationException.class, ExceptionHandler.class})
public class NuitDuplicationExceptionHandler implements ExceptionHandler<NuitDuplicationException, HttpResponse<MentoringAPIError>> {
    @Override
    public HttpResponse<MentoringAPIError> handle(HttpRequest request, NuitDuplicationException exception) {
        return HttpResponse.badRequest(MentoringAPIError.builder()
                                                                .status(HttpStatus.BAD_REQUEST.getCode())
                                                                .error("NUIT_DUPLICADO")
                                                                .message("O NUIT informado ja existe registado no sistema").build());
    }
}
