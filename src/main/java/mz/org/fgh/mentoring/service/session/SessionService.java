package mz.org.fgh.mentoring.service.session;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.repository.session.SessionRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.PerformedSession;
import mz.org.fgh.mentoring.util.SubmitedSessions;

import java.util.Date;
import java.util.List;

@Singleton
public class SessionService {

    SessionRepository sessionRepository;

    public List<SubmitedSessions> findNumberOfSessionsPerDistrict() {
        return this.sessionRepository.findNumberOfSessionsPerDistrict(LifeCycleStatus.ACTIVE);
    }

    public List<SubmitedSessions> findNumberOfSessionsPerDistrict(String tutoruuid) {
        return this.sessionRepository.findNumberOfSessionsPerDistrict(tutoruuid, LifeCycleStatus.ACTIVE);
    }

    public List<PerformedSession> findPerformedSessionsBySelectedFilter(String districtUuid, String healthFacilityUuid, String formUuid, String programmaticAreaUuid, String tutorUuid, String cabinetUuid, Date startDate, Date endDate) {
        return this.sessionRepository.findMacthingSelectedFilter(districtUuid, healthFacilityUuid, programmaticAreaUuid, formUuid, tutorUuid, cabinetUuid, startDate, endDate, LifeCycleStatus.ACTIVE);
    }

    public List<PerformedSession> findPerformedSessionsBySelectedFilterList(String distric, String healthFacility, String form, String programmaticArea, String tutor, String cabinet, Date startDate, Date endDate) {
        return this.sessionRepository.findMacthingSelectedFilterList(distric, healthFacility, programmaticArea, form, tutor, cabinet, startDate, endDate, LifeCycleStatus.ACTIVE);
    }

    public List<PerformedSession> findPerformedSessionsByTutorAndForm(String tutorUuid, String formUuid, Date startDate, Date endDate) {
        return this.sessionRepository.findMathingTutorAndForm(tutorUuid, formUuid, startDate, endDate);
    }

    public List<PerformedSession> findPerformedSessionsByTutor(String tutorUuid, Date startDate, Date endDate) {
        return this.sessionRepository.findMatchingTutor(tutorUuid, startDate, endDate);
    }

    public List<PerformedSession> findPerformedSessionsBySelectedFilterHTS(Date startDate, Date endDate) {
        return this.sessionRepository.findMatchingSelectedFilterHTS(startDate, endDate);
    }

    public List<PerformedSession> findPerformedSessionsBySelectedFilterLast12Months() {
        return this.sessionRepository.findMathingSelectedFilterLast12Months();
    }

    public List<PerformedSession> findPerformedSessionsBySelectedFilterLast12Months(String tutoruuid) {
        return this.sessionRepository.findMathingSelectedFilterLast12Months(tutoruuid);
    }

    public List<PerformedSession> findPerformedSessionsBySelectedFilterNarrative(Date startDate, Date endDate) {
        return this.sessionRepository.findPerformedSessionsMathingSelectedFilterNarrative(startDate, endDate);
    }

    public List<PerformedSession> findPerformedSessionsBySelectedFilterNarrativeCOP20(Date startDate, Date endDate) {
        return this.sessionRepository.findMatchingSelectedFilterNarrativeCOP20(startDate, endDate);
    }

    public List<PerformedSession> findPerformedSessionsBySelectedFilterPMQTR(Date startDate, Date endDate) {
        return this.sessionRepository.findMatchingSelectedFilterPMQTR(startDate, endDate);
    }

    public List<PerformedSession> findPerformedSessionsBySelectedFilterIndicators(Date startDate, Date endDate) {
        return this.sessionRepository.findMatchingSelectedFilterIndicators(startDate, endDate);
    }

    public List<PerformedSession> findPerformedSessionsBySelectedFilterIndicatorsList(Date startDate, Date endDate) {
        return this.sessionRepository.findPerformedSessionsMatchingSelectedFilterIndicatorsList(startDate, endDate);
    }
}
