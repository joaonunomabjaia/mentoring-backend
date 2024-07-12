package mz.org.fgh.mentoring.auth;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpHeaderValues;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.security.token.jwt.render.BearerTokenRenderer;
import io.micronaut.security.token.jwt.render.TokenRenderer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.user.UserDTO;
import mz.org.fgh.mentoring.entity.user.RefreshTokenEntity;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.user.UserRepository;

import java.util.Optional;

@Singleton
@Replaces(value = BearerTokenRenderer.class)
public class MentoringTokenRenderer implements TokenRenderer {

    @Inject
    UserRepository users;

    @Inject
    RefreshTokenRepository refreshTokenRepository;

    private static final String BEARER_TOKEN_TYPE = HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER;

    @Override
    public AccessRefreshToken render(Integer expiresIn, String accessToken, String refreshToken) {
        return new AccessRefreshToken(accessToken, refreshToken, BEARER_TOKEN_TYPE, expiresIn);
    }

    @Override
    public AccessRefreshToken render(Authentication authentication, Integer expiresIn, String accessToken, String refreshToken) {
        MentoringAccessRefreshToken token =  new MentoringAccessRefreshToken(authentication.getName(), authentication.getRoles(), expiresIn, accessToken, refreshToken, BEARER_TOKEN_TYPE);

        RefreshTokenEntity existingRefreshToken =  refreshTokenRepository.findByUsernameAndRevoked(authentication.getName(), false);
        if (existingRefreshToken != null) {
            existingRefreshToken.setRefreshToken(refreshToken);
            refreshTokenRepository.updateByUsername(existingRefreshToken, authentication.getName());
        }

        Optional<User> optUser = this.users.findById((Long) authentication.getAttributes().get("userInfo"));
        optUser.ifPresent(user -> token.setUserInfo(new UserDTO(user)));
        optUser.ifPresent(user ->token.setUserUuid(optUser.get().getUuid()));
        return token;
    }
}
