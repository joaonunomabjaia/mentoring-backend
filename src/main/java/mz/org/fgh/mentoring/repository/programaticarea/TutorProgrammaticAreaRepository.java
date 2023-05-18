package mz.org.fgh.mentoring.repository.programaticarea;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;

@Repository
public interface TutorProgrammaticAreaRepository extends CrudRepository<TutorProgrammaticArea, Long> {
    @Override
    TutorProgrammaticArea save(TutorProgrammaticArea tutorProgrammaticArea);
    @Override
    TutorProgrammaticArea update(TutorProgrammaticArea tutorProgrammaticArea);
}
