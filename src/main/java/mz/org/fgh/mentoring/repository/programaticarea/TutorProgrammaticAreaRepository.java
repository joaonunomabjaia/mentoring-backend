package mz.org.fgh.mentoring.repository.programaticarea;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;

import java.util.List;

@Repository
public interface TutorProgrammaticAreaRepository extends CrudRepository<TutorProgrammaticArea, Long> {
    @Override
    List<TutorProgrammaticArea> findAll();
    //    @Query("select t from TutorProgrammaticArea t where t.tutor.id = :tutorId")
    @Query("select t from TutorProgrammaticArea t join fetch t.tutor join fetch t.programmaticArea where t.tutor.id = :tutorId")

    public List<TutorProgrammaticArea> fetchAll(final Long tutorId);
}
