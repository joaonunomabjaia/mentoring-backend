package mz.org.fgh.mentoring.repository.programaticarea;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;

import java.util.List;

@Repository
public interface TutorProgrammaticAreaRepository extends CrudRepository<TutorProgrammaticArea, Long> {
    @Override
    List<TutorProgrammaticArea> findAll();
}
