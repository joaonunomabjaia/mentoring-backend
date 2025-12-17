package mz.org.fgh.mentoring.repository.tutor;


import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import mz.org.fgh.mentoring.entity.tutored.PasswordReset;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByToken(String token);

    List<PasswordReset> findByEmailAndUsedFalse(String email);

    void deleteByEmail(String email);

    List<PasswordReset> findByUsedFalseAndExpiresAtBefore(Date now);

    // ✅ DELETE direto e retorna quantidade apagada
    long deleteByUsedFalseAndExpiresAtBefore(Date now);
}