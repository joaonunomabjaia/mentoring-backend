package mz.org.fgh.mentoring.auth;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.user.RefreshTokenEntity;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Optional;

import static io.micronaut.security.errors.IssuingAnAccessTokenErrorCode.INVALID_GRANT;

@Singleton
public class RefreshTokenHandler implements RefreshTokenPersistence {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenHandler(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void persistToken(RefreshTokenGeneratedEvent event) {
        if (event != null &&
                event.getRefreshToken() != null &&
                event.getAuthentication() != null &&
                event.getAuthentication().getName() != null) {
            String payload = event.getRefreshToken();
            RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(event.getAuthentication().getName(), payload, false);
            Optional<RefreshTokenEntity> optRefreshToken = refreshTokenRepository.findByUsernameAndRevoked(event.getAuthentication().getName(), false);
            if (optRefreshToken.isPresent()) {
                RefreshTokenEntity existingRefreshToken =    optRefreshToken.get();
                existingRefreshToken.setRevoked(true);
                refreshTokenRepository.updateByUsername(existingRefreshToken, event.getAuthentication().getName());
            }
            refreshTokenRepository.save(refreshTokenEntity);

        }
    }


    public Publisher<Authentication> getAuthentication(String refreshToken) {
        return Flux.create(emitter -> {
            Optional<RefreshTokenEntity> tokenOpt = refreshTokenRepository.findByRefreshToken(refreshToken);
            if (tokenOpt.isPresent()) {
                RefreshTokenEntity token = tokenOpt.get();
                if (token.getRevoked()) {
                    emitter.error(new OauthErrorResponseException(INVALID_GRANT, "refresh token revoked", null)); // <5>
                } else {
                    emitter.next(Authentication.build(token.getUsername())); // <6>
                    emitter.complete();
                }
            } else {
                emitter.error(new OauthErrorResponseException(INVALID_GRANT, "refresh token not found", null)); // <7>
            }
        }, FluxSink.OverflowStrategy.ERROR);
    }
}
