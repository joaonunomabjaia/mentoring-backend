package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


@Repository
public interface TutorRepository extends CrudRepository<Tutor, Long> {

    @Override
    List<Tutor> findAll();

    @Override
    Optional<Tutor> findById(@NotNull Long id);

}
