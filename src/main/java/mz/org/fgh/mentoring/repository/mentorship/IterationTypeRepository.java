package mz.org.fgh.mentoring.repository.mentorship;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.mentorship.IterationType;

import java.util.List;
import java.util.Optional;

@Repository
public interface IterationTypeRepository extends CrudRepository<IterationType, Long> {

    List<IterationType> findAll();
    IterationType getByCode(String code);
    Optional<IterationType> findByUuid(String uuid);
}
