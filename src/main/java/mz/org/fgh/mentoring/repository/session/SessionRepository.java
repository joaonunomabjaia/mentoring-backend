package mz.org.fgh.mentoring.repository.session;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.location.District;
import mz.org.fgh.mentoring.entity.programaticarea.ProgramaticArea;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.PerformedSession;
import mz.org.fgh.mentoring.util.SubmitedSessions;

import java.util.Date;
import java.util.List;


public interface SessionRepository extends CrudRepository<Session, Long> {
    List<SubmitedSessions> findNumberOfSessionsPerDistrict(LifeCycleStatus active);

    Session findByUuid(String uuid);
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
}
