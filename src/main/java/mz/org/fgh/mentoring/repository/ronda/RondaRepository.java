package mz.org.fgh.mentoring.repository.ronda;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface RondaRepository extends CrudRepository<Ronda, Long> {

    @Override
    List<Ronda> findAll();

    @Override
    Optional<Ronda> findById(@NotNull Long id);

    Optional<Ronda> findByUuid(String uuid);

    @Query(value = "select * from rondas limit :limi offset :off", nativeQuery = true)
    List<Ronda> findRondaWithLimit(long limi, long off);

    @Query("select DISTINCT(r) from RondaMentor rm " +
            "INNER JOIN FETCH rm.ronda r " +
            "INNER JOIN FETCH pa.rondaType rt " +
            "where rm.id =: mentorId AND rm.lifeCycleStatus = :lifeCycleStatus AND r.lifeCycleStatus = :lifeCycleStatus ")
    List<Ronda> getAllRondasOfMentor(Long mentorId, LifeCycleStatus lifeCycleStatus);
}
