package mz.org.fgh.mentoring.auth;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import lombok.Getter;
import lombok.Setter;
import mz.org.fgh.mentoring.entity.user.UserDTO;

import java.util.Collection;

@Introspected
@Getter
@Setter
public class MentoringAccessRefreshToken extends BearerAccessRefreshToken {

    private UserDTO userInfo;

    public MentoringAccessRefreshToken(String username,
                                       Collection<String> roles,
                                       Integer expiresIn,
                                       String accessToken,
                                       String refreshToken,
                                       String tokenType
                                        ) {
        super(username, roles, expiresIn, accessToken, refreshToken, tokenType);
    }
}
