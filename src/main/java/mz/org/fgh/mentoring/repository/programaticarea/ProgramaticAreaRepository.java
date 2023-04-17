package mz.org.fgh.mentoring.repository.programaticarea;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.programaticarea.ProgramaticArea;

@Repository
public interface ProgramaticAreaRepository extends CrudRepository<ProgramaticArea, Long> {
}
