package mz.org.fgh.mentoring.repository.mentorship;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface MentorshipRepository extends CrudRepository<Mentorship, Long> {

    @Override
    List<Mentorship> findAll();

    @Override
    Optional<Mentorship> findById(@NotNull Long id);
}
