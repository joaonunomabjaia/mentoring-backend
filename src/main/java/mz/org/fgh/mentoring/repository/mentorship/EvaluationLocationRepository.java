package mz.org.fgh.mentoring.repository.mentorship;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import mz.org.fgh.mentoring.entity.mentorship.EvaluationLocation;

import java.util.Optional;

@Repository
public interface EvaluationLocationRepository extends JpaRepository<EvaluationLocation, Long> {
    Optional<EvaluationLocation> findByCode(String code);

    Optional<EvaluationLocation> findByUuid(String uuid);
}
