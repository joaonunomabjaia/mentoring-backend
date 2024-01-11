package mz.org.fgh.mentoring.auth;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpHeaderValues;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.security.token.jwt.render.BearerTokenRenderer;
import io.micronaut.security.token.jwt.render.TokenRenderer;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.user.UserDTO;

@Singleton
@Replaces(value = BearerTokenRenderer.class)
public class MentoringTokenRenderer implements TokenRenderer {

    private static final String BEARER_TOKEN_TYPE = HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER;

    @Override
    public AccessRefreshToken render(Integer expiresIn, String accessToken, String refreshToken) {
        return new AccessRefreshToken(accessToken, refreshToken, BEARER_TOKEN_TYPE, expiresIn);
    }

    @Override
    public AccessRefreshToken render(Authentication authentication, Integer expiresIn, String accessToken, String refreshToken) {
        MentoringAccessRefreshToken token =  new MentoringAccessRefreshToken(authentication.getName(), authentication.getRoles(), expiresIn, accessToken, refreshToken, BEARER_TOKEN_TYPE);
        token.setUserInfo((UserDTO) authentication.getAttributes().get("userInfo"));
        return token;
    }
}
