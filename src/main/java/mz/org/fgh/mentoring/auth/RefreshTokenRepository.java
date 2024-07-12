package mz.org.fgh.mentoring.auth;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.user.RefreshTokenEntity;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, Long> {

    @Transactional
    RefreshTokenEntity save(RefreshTokenEntity refreshTokenEntity);

    Optional<RefreshTokenEntity> findByRefreshToken(@NonNull @NotBlank String refreshToken);


    @Transactional
    RefreshTokenEntity save(@NonNull @NotBlank String username,
                            @NonNull @NotBlank String refreshToken,
                            @NonNull @NotNull Boolean revoked);


    @Transactional
    RefreshTokenEntity updateByUsername(RefreshTokenEntity refreshTokenEntity, String userName);

    RefreshTokenEntity findByUsernameAndRevoked(String userName, Boolean revoked);

    long updateByUsername(@NonNull @NotBlank String username, boolean revoked);
}
