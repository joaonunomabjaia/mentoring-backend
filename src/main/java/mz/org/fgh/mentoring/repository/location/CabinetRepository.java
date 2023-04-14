package mz.org.fgh.mentoring.repository.location;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface CabinetRepository extends CrudRepository<Cabinet, Long> {

    @Override
    List<Cabinet> findAll();

    @Override
    Optional<Cabinet> findById(@NotNull Long id);
}
