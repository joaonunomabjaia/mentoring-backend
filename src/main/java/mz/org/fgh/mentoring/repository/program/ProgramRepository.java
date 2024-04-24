package mz.org.fgh.mentoring.repository.program;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.user.User;

import java.util.List;

/**
 * @author Jose Julai Ritsure
 */
@Repository
public interface ProgramRepository extends CrudRepository<Program, Long> {
    @Override
    List<Program> findAll();
    List<Program> search(String name, String description);
}
