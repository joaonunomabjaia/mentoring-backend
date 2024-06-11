package mz.org.fgh.mentoring.repository.ronda;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.ronda.RondaMentor;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface RondaMentorRepository extends CrudRepository<RondaMentor, Long> {

    @Override
    List<RondaMentor> findAll();

    @Override
    Optional<RondaMentor> findById(@NotNull Long id);

    Optional<RondaMentor> findByUuid(String uuid);

    @Query("select rm from RondaMentor rm " +
            "INNER JOIN FETCH rm.ronda r " +
            "INNER JOIN FETCH rm.mentor m " +
            "INNER JOIN FETCH m.tutorProgrammaticAreas tpa " +
            "where r.id = :rondaId AND rm.lifeCycleStatus = :lifeCycleStatus ")
    List<RondaMentor> findByRonda(Long rondaId, LifeCycleStatus lifeCycleStatus);
}
