package mz.org.fgh.mentoring.repository.program;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.program.Program;

import java.util.List;
import java.util.Optional;

/**
 * @author Jose Julai Ritsure
 */
@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    List<Program> search(String name, String description);

    Page<Program> findByNameIlike(String name, Pageable pageable);

    Optional<Program> findByUuid(String uuid);
}
