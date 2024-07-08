package mz.org.fgh.mentoring.repository.ronda;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaMentee;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

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
            "where rm.ronda.id = :rondaId")
    List<RondaMentee> findByRonda(Long rondaId);

    @Query("delete from RondaMentee rm where rm.ronda = :ronda")
    void deleteAllOfRonda(Ronda ronda);
}
