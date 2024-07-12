package mz.org.fgh.mentoring.auth;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.entity.user.RefreshTokenEntity;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/auth")
public class AuthController {

    @Inject
    private JwtTokenGenerator jwtTokenGenerator;
    @Inject
    private RefreshTokenRepository refreshTokenRepository;
    @Inject
    private UserRepository userRepository;

    /**
     * Endpoint to refresh JWT tokens.
     */

    @Post("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public Publisher<AccessRefreshToken> refreshAccessToken(@Body String refreshToken) {
        return Flux.create(emitter -> {
            Optional<RefreshTokenEntity> tokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken);
            if (tokenEntity.isPresent() && !tokenEntity.get().getRevoked()) {
                Authentication authentication = Authentication.build(tokenEntity.get().getUsername());

                Optional<User> user = userRepository.findByUsername(authentication.getName());

                String username = tokenEntity.get().getUsername();
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("sub", username);
                attributes.put("userInfo", user.get().getId());

                String newAccessToken = jwtTokenGenerator.generateToken(attributes).get();

                Map<String, Object> refreshTokenAttributes = new HashMap<>(attributes);
                refreshTokenAttributes.put("type", "refresh");
                String newRefreshToken = jwtTokenGenerator.generateToken(refreshTokenAttributes).get();

                // Save the new refresh token to the database
                RefreshTokenEntity newTokenEntity = new RefreshTokenEntity();
                newTokenEntity.setUsername(tokenEntity.get().getUsername());
                newTokenEntity.setRefreshToken(newRefreshToken);
                newTokenEntity.setDateCreated(DateUtils.getCurrentDate().toInstant());
                newTokenEntity.setRevoked(false);

                RefreshTokenEntity oldToken = tokenEntity.get();
                oldToken.setRevoked(true);
                refreshTokenRepository.update(oldToken);

                refreshTokenRepository.save(newTokenEntity);

                emitter.next(new AccessRefreshToken(newAccessToken, newRefreshToken, "Bearer"));
                emitter.complete();
            } else {
                emitter.error(new HttpStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token"));
            }
        });
    }


    private boolean validateRefreshToken(String refreshToken) {
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken);
        return refreshTokenEntity.isPresent() && !refreshTokenEntity.get().getRevoked();
    }


    private Authentication buildAuthenticationFromRefreshToken(String refreshToken) {
        Optional<RefreshTokenEntity> tokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (tokenEntity.isPresent()) {
            return Authentication.build(tokenEntity.get().getUsername());
        }
        throw new IllegalStateException("Refresh token is valid but no user found.");
    }
}
