package mz.org.fgh.mentoring.repository.mentorship;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.entity.mentorship.EvaluationType;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.session.Session;
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

    Optional<Mentorship> findByUuid(String uuid);

    @Query("SELECT m FROM Mentorship m " +
            "INNER JOIN FETCH m.tutor t " +
            "INNER JOIN FETCH m.tutored td " +
            "INNER JOIN FETCH m.form f " +
            "INNER JOIN FETCH m.cabinet c " +
            "INNER JOIN FETCH m.evaluationType et " +
            "INNER JOIN FETCH m.evaluationLocation el " +
            "INNER JOIN FETCH m.door d " +
            "INNER JOIN FETCH m.session s " +
            "WHERE s.uuid = :sessionUuid ")
    List<Mentorship> fetchBySessionUuid(String sessionUuid, LifeCycleStatus lifeCycleStatus);

    List<Mentorship> findBySession(Session session);

    @Query("SELECT m FROM Mentorship m " +
            "JOIN FETCH m.tutored t " +
            "JOIN FETCH t.employee e " +
            "WHERE m.session = :session " +
            "AND m.evaluationType = :evaluationType " +
            "ORDER BY m.performedDate DESC")
    List<Mentorship> fetchLatestBySessionAndEvaluationType(Session session, EvaluationType evaluationType, Pageable pageable);

    long countByCabinet(Cabinet cabinet);

    /**
     * Última mentoria realizada por um mentee numa determinada ronda.
     */
    @Query("SELECT MAX(m.performedDate) FROM Mentorship m " +
            "JOIN m.session s " +
            "WHERE s.ronda.id = :rondaId " +
            "AND m.tutored.id = :tutoredId " +
            "AND m.lifeCycleStatus = :lifeCycleStatus")
    Optional<Date> findLastPerformedDateByRondaAndTutored(Long rondaId,
                                                          Long tutoredId,
                                                          LifeCycleStatus lifeCycleStatus);

}
