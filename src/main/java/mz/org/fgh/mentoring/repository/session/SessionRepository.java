package mz.org.fgh.mentoring.repository.session;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.PerformedSession;
import mz.org.fgh.mentoring.util.SubmitedSessions;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface SessionRepository extends CrudRepository<Session, Long> {
    List<SubmitedSessions> findNumberOfSessionsPerDistrict(LifeCycleStatus active);

    Optional<Session> findByUuid(String uuid);
    List<SubmitedSessions> findNumberOfSessionsPerDistrict(String tutoruuid, LifeCycleStatus active);

    List<PerformedSession> findMacthingSelectedFilter(String district, String healthFacility, String programmaticArea, String form, String tutor, String cabinet, Date startDate, Date endDate, LifeCycleStatus lifeCycleStatus);

    List<PerformedSession> findMacthingSelectedFilterList(String distric, String healthFacility, String programmaticArea, String form, String tutor, String cabinet, Date startDate, Date endDate, LifeCycleStatus active);

    List<PerformedSession> findMathingTutorAndForm(String tutorUuid, String formUuid, Date startDate, Date endDate);

    List<PerformedSession> findMatchingTutor(String tutorUuid, Date startDate, Date endDate);

    List<PerformedSession> findMatchingSelectedFilterHTS(Date startDate, Date endDate);

    List<PerformedSession> findMathingSelectedFilterLast12Months();

    List<PerformedSession> findMathingSelectedFilterLast12Months(String tutoruuid);

    List<PerformedSession> findPerformedSessionsMathingSelectedFilterNarrative(Date startDate, Date endDate);

    List<PerformedSession> findMatchingSelectedFilterNarrativeCOP20(Date startDate, Date endDate);

    List<PerformedSession> findMatchingSelectedFilterPMQTR(Date startDate, Date endDate);

    List<PerformedSession> findMatchingSelectedFilterIndicators(Date startDate, Date endDate);

    List<PerformedSession> findPerformedSessionsMatchingSelectedFilterIndicatorsList(Date startDate, Date endDate);

    @Query("SELECT s FROM Session s " +
            "JOIN s.ronda r " +
            "JOIN r.rondaType rt " +
            "JOIN r.rondaMentees rm " +
            "JOIN rm.tutored td " +
            "WHERE rt.code = 'SESSAO_ZERO' " +
            "AND td.id = :tutoredId ")
    Optional<Session> getTutoredZeroSession(Long tutoredId);

    @Query("SELECT s FROM Session s " +
            "INNER JOIN FETCH s.ronda r " +
            "INNER JOIN FETCH s.mentorships m " +
            "INNER JOIN FETCH s.status st " +
            "INNER JOIN FETCH m.tutor t " +
            "INNER JOIN FETCH m.tutored td " +
            "INNER JOIN FETCH m.form f " +
            "INNER JOIN FETCH m.cabinet c " +
            "INNER JOIN FETCH m.evaluationType et " +
            "INNER JOIN FETCH m.door d " +
            "WHERE r.id = :rondaId ")
    Optional<Session> findByRonda(Long rondaId);

    @Query("SELECT s FROM Session s " +
            "INNER JOIN FETCH s.ronda r " +
            "INNER JOIN FETCH s.mentorships m " +
            "INNER JOIN FETCH s.status st " +
            "INNER JOIN FETCH m.tutor t " +
            "INNER JOIN FETCH m.tutored td " +
            "WHERE s.startDate =:startDate " +
            "AND  s.endDate is null ")
    List<Session> getAllOfRondaPending(Date startDate);
}
