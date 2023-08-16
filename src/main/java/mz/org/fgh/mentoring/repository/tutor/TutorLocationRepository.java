package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutor.TutorLocation;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface TutorLocationRepository extends CrudRepository<TutorLocation, Long> {

    @Override
    List<TutorLocation> findAll();

    @Override
    Optional<TutorLocation> findById(@NotNull Long id);

    List<TutorLocation> findByTutor(final Tutor tutor);
}
