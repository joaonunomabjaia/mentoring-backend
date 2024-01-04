package mz.org.fgh.mentoring.entity.user;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.DateCreated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.MappedEntity;
import lombok.ToString;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;


@Schema(name = "RefreshTokenEntity", description = "Entidade responsavel em guardar os tokens para refresh")
@Entity(name = "RefreshTokenEntity")
@Table(name = "refresh_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "ID",
            nullable = false
    )
    private Long id;

    @NonNull
    @NotBlank
    @Column(name = "USER_NAME", nullable = false, length = 100)
    private String username;

    @NonNull
    @NotBlank
    @Column(name = "REFRESH_TOKEN", nullable = false, length = 500)
    private String refreshToken;

    @NonNull
    @NotNull
    @Column(name = "REVOKED", nullable = false)
    private Boolean revoked;

    @DateCreated // <4>
    @NonNull
    @NotNull
    @Column(name = "CREATED_AT", nullable = false)
    private Instant dateCreated;

    public RefreshTokenEntity(@NonNull String username, @NonNull String refreshToken, @NonNull Boolean revoked) {
        this.username = username;
        this.refreshToken = refreshToken;
        this.revoked = revoked;
    }
}
