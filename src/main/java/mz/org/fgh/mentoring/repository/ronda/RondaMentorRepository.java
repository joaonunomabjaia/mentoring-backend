package mz.org.fgh.mentoring.repository.ronda;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaMentor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RondaMentorRepository extends CrudRepository<RondaMentor, Long> {

    @Override
    List<RondaMentor> findAll();

    @Override
    Optional<RondaMentor> findById(@NotNull Long id);

    Optional<RondaMentor> findByUuid(String uuid);

    @Query("select rm from RondaMentor rm " +
            "where rm.ronda.id = :rondaId")
    Set<RondaMentor> findByRonda(Long rondaId);

    void deleteByRonda(Ronda ronda);

    @Query("SELECT DISTINCT rm FROM RondaMentor rm " +
            "JOIN FETCH rm.mentor " +
            "JOIN FETCH rm.ronda r " +
            "WHERE r.id = :rondaId")
    Set<RondaMentor> findMentorsForRondas(Long rondaId);
}
