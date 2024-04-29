package mz.org.fgh.mentoring.repository.programaticarea;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface TutorProgrammaticAreaRepository extends CrudRepository<TutorProgrammaticArea, Long> {
    @Override
    List<TutorProgrammaticArea> findAll();

    Optional<TutorProgrammaticArea> findByUuid(@NotNull String uuid);

    @Query("select t from TutorProgrammaticArea t join fetch t.tutor join fetch t.programmaticArea where t.tutor.id = :tutorId")

    public List<TutorProgrammaticArea> fetchAll(final Long tutorId);
}
