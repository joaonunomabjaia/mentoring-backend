package mz.org.fgh.mentoring.repository.resource;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.earesource.Resource;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, Long> {

    @Override
    List<Resource> findAll();

    Optional<Resource> findByUuid(@NotNull String uuid);
}
