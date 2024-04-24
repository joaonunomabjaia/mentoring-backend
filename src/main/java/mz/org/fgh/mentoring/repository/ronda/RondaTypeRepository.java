package mz.org.fgh.mentoring.repository.ronda;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaType;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface RondaTypeRepository extends CrudRepository<RondaType, Long> {

    @Override
    List<RondaType> findAll();

    @Override
    Optional<RondaType> findById(@NotNull Long id);

    Optional<RondaType> findByUuid(String uuid);

    RondaType findByCode(String code);
}
