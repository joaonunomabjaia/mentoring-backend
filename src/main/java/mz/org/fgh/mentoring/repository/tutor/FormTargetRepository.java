package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.formtarget.FormTarget;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.List;

@Repository
public interface FormTargetRepository extends CrudRepository<FormTarget, Long> {

    @Query("SELECT ft FROM FormTarget ft INNER JOIN FETCH ft.career c INNER JOIN FETCH ft.form f INNER JOIN FETCH f.programmaticArea INNER JOIN c.tutors t WHERE t.uuid = :tutorUuid AND ft.lifeCycleStatus =:lifeCycleStatus")
    List<FormTarget> findFormTargetByTutor( String tutorUuid, LifeCycleStatus lifeCycleStatus);
}
