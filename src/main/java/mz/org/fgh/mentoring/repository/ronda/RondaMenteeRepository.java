package mz.org.fgh.mentoring.repository.ronda;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaMentee;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RondaMenteeRepository extends CrudRepository<RondaMentee, Long> {

    @Override
    List<RondaMentee> findAll();

    @Override
    Optional<RondaMentee> findById(@NotNull Long id);

    Optional<RondaMentee> findByUuid(String uuid);

    @Query("select rm from RondaMentee rm " +
            "INNER JOIN FETCH rm.ronda r " +
            "INNER JOIN FETCH rm.tutored t " +
            "where r.id = :rondaId AND rm.lifeCycleStatus = :lifeCycleStatus ")
    List<RondaMentee> findByRonda(Long rondaId, LifeCycleStatus lifeCycleStatus);

    @Query("select rm from RondaMentee rm " +
            "JOIN FETCH rm.tutored t " +
            "LEFT JOIN FETCH t.menteeFlowHistories mfh " +
            "where rm.ronda.id = :rondaId")
    Set<RondaMentee> findByRonda(Long rondaId);

    void deleteByRonda(Ronda ronda);

    @Query("SELECT DISTINCT rm FROM RondaMentee rm " +
            "JOIN FETCH rm.tutored t " +
            "LEFT JOIN FETCH t.menteeFlowHistories mfh " +
            "JOIN FETCH rm.ronda r " +
            "WHERE r.id = :rondaId")
    Set<RondaMentee> findMenteesForRondas(Long rondaId);

    Optional<RondaMentee> findByRondaAndTutored(Ronda ronda, Tutored tutored);
}
