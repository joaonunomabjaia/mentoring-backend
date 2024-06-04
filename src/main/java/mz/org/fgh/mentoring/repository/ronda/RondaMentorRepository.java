package mz.org.fgh.mentoring.repository.ronda;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.ronda.RondaMentor;

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

}
