package mz.org.fgh.mentoring.repository.mentorship;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.mentorship.IterationType;

import java.util.List;

@Repository
public interface IterationTypeRepository extends CrudRepository<IterationType, Long> {

    List<IterationType> findAll();
    IterationType getByCode(String code);
}
