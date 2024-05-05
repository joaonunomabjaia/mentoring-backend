package mz.org.fgh.mentoring.repository.mentorship;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.mentorship.IterationType;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.PerformedSession;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface MentorshipRepository extends CrudRepository<Mentorship, Long> {

    @Override
    List<Mentorship> findAll();

    @Override
    Optional<Mentorship> findById(@NotNull Long id);

    List<PerformedSession> getSelectedOfFilterPMQTRList(Date startDate, Date endDate);

    List<Mentorship> fetchBySelectedFilter(String code, String tutor, String tutored, String formName, String healthFacility, String iterationType, Integer iterationNumber, LifeCycleStatus lfStatus, Date performedStartDate, Date performedEndDate);

    @Query("select DISTINCT(m) from RondaMentor rm " +
            "INNER JOIN FETCH rm.mentor m " +
            "INNER JOIN FETCH m.tutor t " +
            "INNER JOIN FETCH m.tutored td " +
            "INNER JOIN FETCH m.form f " +
            "INNER JOIN FETCH m.healthFacility hf " +
            "INNER JOIN FETCH m.session s " +
            "INNER JOIN FETCH m.cabinet c " +
            "INNER JOIN FETCH m.iterationType it " +
            "INNER JOIN FETCH m.door d " +
            "INNER JOIN FETCH m.timeOfDay tOd " +
            "where rm.id = :mentorId AND rm.lifeCycleStatus = :lifeCycleStatus AND m.lifeCycleStatus = :lifeCycleStatus ")
    List<Mentorship> getAllMentorshipSessionsOfMentor(Long mentorId, LifeCycleStatus lifeCycleStatus);
}
