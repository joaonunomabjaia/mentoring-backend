package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.career.Career;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface CareerRepository extends CrudRepository<Career, Long> {

    List<Career> findAll();

    Optional<Career> findById(@NotNull Long id);

}
