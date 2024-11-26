package mz.org.fgh.mentoring.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public AuthController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Endpoint to refresh JWT tokens.
     */

    @Post("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public Publisher<AccessRefreshToken> refreshAccessToken(@Body String body) {
        return Flux.create(emitter -> {
            String refreshToken;
            try {
                JsonNode jsonNode = objectMapper.readTree(body);
                refreshToken = jsonNode.get("refresh_token").asText();
            } catch (JsonProcessingException e) {
                emitter.error(new HttpStatusException(HttpStatus.BAD_REQUEST, "Invalid refresh token format"));
                return;
            }

            Optional<RefreshTokenEntity> tokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken);
            if (tokenEntity.isPresent()) {
                Authentication authentication = Authentication.build(tokenEntity.get().getUsername());
                Optional<User> user = userRepository.findByUsername(authentication.getName());

                if (user.isEmpty()) {
                    emitter.error(new HttpStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
                    return;
                }

                String username = tokenEntity.get().getUsername();
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("sub", username);
                attributes.put("userInfo", user.get().getId());

                String newAccessToken = jwtTokenGenerator.generateToken(attributes).get();

                Map<String, Object> refreshTokenAttributes = new HashMap<>(attributes);
                refreshTokenAttributes.put("type", "refresh");
                refreshTokenAttributes.put("time", DateUtils.getCurrentDate());
                String newRefreshToken = jwtTokenGenerator.generateToken(refreshTokenAttributes).get();

                // Mark old token as revoked
                RefreshTokenEntity oldToken = tokenEntity.get();
                oldToken.setRevoked(true);
                refreshTokenRepository.update(oldToken);

                // Check if the new refresh token already exists
                Optional<RefreshTokenEntity> existingToken = refreshTokenRepository.findByRefreshToken(newRefreshToken);
                if (existingToken.isPresent()) {
                    // Update the existing token instead of inserting a new one
                    RefreshTokenEntity existing = existingToken.get();
                    existing.setDateCreated(DateUtils.getCurrentDate().toInstant());
                    existing.setRevoked(false);
                    refreshTokenRepository.update(existing);
                } else {
                    // Save the new refresh token
                    RefreshTokenEntity newTokenEntity = new RefreshTokenEntity();
                    newTokenEntity.setUsername(username);
                    newTokenEntity.setRefreshToken(newRefreshToken);
                    newTokenEntity.setDateCreated(DateUtils.getCurrentDate().toInstant());
                    newTokenEntity.setRevoked(false);

                    refreshTokenRepository.save(newTokenEntity);
                }

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
