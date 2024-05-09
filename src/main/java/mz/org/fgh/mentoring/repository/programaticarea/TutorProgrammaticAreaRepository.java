package mz.org.fgh.mentoring.repository.programaticarea;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.entity.user.User;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface TutorProgrammaticAreaRepository extends CrudRepository<TutorProgrammaticArea, Long> {

    TutorProgrammaticArea createOrUpdate(TutorProgrammaticArea tutorProgrammaticArea, User user, Tutor tutor);

    @Override
    List<TutorProgrammaticArea> findAll();

    Optional<TutorProgrammaticArea> findByUuid(@NotNull String uuid);

    @Query("select t from TutorProgrammaticArea t join fetch t.tutor join fetch t.programmaticArea where t.tutor.id = :tutorId")
    public List<TutorProgrammaticArea> getAllByTutorId(final Long tutorId);
}
