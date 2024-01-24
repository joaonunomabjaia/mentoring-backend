package mz.org.fgh.mentoring.repository.program;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.program.Program;

import java.util.List;

/**
 * @author Jose Julai Ritsure
 */
@Repository
public interface ProgramRepository extends CrudRepository<Program, Long> {
    @Override
    List<Program> findAll();
}
