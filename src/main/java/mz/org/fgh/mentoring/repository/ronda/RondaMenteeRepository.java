package mz.org.fgh.mentoring.repository.ronda;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.ronda.RondaMentee;

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

}
