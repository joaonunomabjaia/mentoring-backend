package mz.org.fgh.mentoring.auth;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Repository;
import mz.org.fgh.mentoring.entity.user.RefreshTokenEntity;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository {

    @Transactional
    RefreshTokenEntity save(RefreshTokenEntity refreshTokenEntity);

    Optional<RefreshTokenEntity> findByRefreshToken(@NonNull @NotBlank String refreshToken);


    //long updateByUsername(@NonNull @NotBlank String username, boolean revoked);
}
