package mz.org.fgh.mentoring.service.mentorship;

import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.fgh.mentoring.entity.mentorship.IterationType;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.location.CabinetRepository;
import mz.org.fgh.mentoring.repository.mentorship.MentorshipRepository;
import mz.org.fgh.mentoring.repository.session.SessionRepository;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.tutored.TutoredRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.PerformedSession;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Singleton
public class MentorshipService {

    private final MentorshipRepository mentorshipRepository;
    @Inject
    private SessionRepository sessionRepository;
    FormRepository formRepository;
    TutorRepository tutorRepository;
    TutoredRepository tutoredRepository;
    CabinetRepository cabinetRepository;
    HealthFacilityRepository healthFacilityRepository;

    public MentorshipService(MentorshipRepository mentorshipRepository){
        this.mentorshipRepository = mentorshipRepository;
    }

    public Mentorship createMentorship(Mentorship mentorship){
        if(StringUtils.isEmpty(mentorship.getCode()) && mentorship.getHealthFacility() == null && mentorship.getTutor() == null && mentorship.getTutored() == null){
            throw new MentoringBusinessException("Fields 'CODE', 'HEALTH FACILITY', 'TUTOR' and 'TUTORED' are required.");
        }
        return mentorshipRepository.save(mentorship);
    }

    public List<Session> synchronizeMentorships(final List<Session> sessions) {
        for (final Session session : sessions) {
            Session se = this.sessionRepository.findByUuid(session.getUuid());

            if (se == null) {
                Session newSession = sessionRepository.save(session);
                for (final Mentorship mentorship : session.getMentorships()) {
                    mentorship.setSession(newSession);
                    mentorship.setForm(formRepository.findByUuid(mentorship.getForm().getUuid()).get());
                    mentorship.setHealthFacility(healthFacilityRepository.findByUuid(mentorship.getHealthFacility().getUuid()).get());
                    mentorship.setTutor(tutorRepository.findByUuid(mentorship.getTutor().getUuid()).get());
                    mentorship.setTutored(tutoredRepository.findByUuid(mentorship.getTutored().getUuid()).get());
                    mentorship.setCabinet(cabinetRepository.findByUuid(mentorship.getCabinet().getUuid()).get());

                    mentorshipRepository.save(mentorship);
                }

            }
        }
        return sessions;
    }

    public Mentorship findMentorshipById(@NotNull Long id){
        Optional<Mentorship> optionalMentorship = mentorshipRepository.findById(id);
        if(optionalMentorship.isEmpty()){
            throw new MentoringBusinessException("Mentorship with ID: "+id+" was not found.");
        }
        return optionalMentorship.get();
    }

    public List<Mentorship> findAllMentorships(){
        return mentorshipRepository.findAll();
    }

    public List<PerformedSession> findPerformedSessionsBySelectedFilterPMQTRList(Date startDate, Date endDate) {
        return this.mentorshipRepository.getSelectedOfFilterPMQTRList(startDate, endDate);
    }

    public List<Mentorship> fetchBySelectedFilter(String code, String tutor, String tutored,
                                                  String formName, String healthFacility, String iterationType,
                                                  Integer iterationNumber, String lifeCycleStatus, Date performedStartDate,
                                                  Date performedEndDate) {
        LifeCycleStatus lfStatus = null;
        if(lifeCycleStatus != null) {
            try {
                lfStatus = LifeCycleStatus.valueOf(lifeCycleStatus.toUpperCase());
            } catch(IllegalArgumentException iae) {
                // Ignore
            }
        }

        return this.mentorshipRepository.fetchBySelectedFilter(code, tutor, tutored, formName, healthFacility, iterationType, iterationNumber, lfStatus, performedStartDate, performedEndDate);
    }

    public List<MentorshipDTO> getAllMentorshipSessionsOfMentor(Long mentorId) {
        List<Mentorship> mentorships = this.mentorshipRepository.getAllMentorshipSessionsOfMentor(mentorId, LifeCycleStatus.ACTIVE);
        List<MentorshipDTO> dtos = new ArrayList<>();
        for (Mentorship mentorship: mentorships) {
            MentorshipDTO mentorshipDTO = new MentorshipDTO(mentorship);
            dtos.add(mentorshipDTO);
        }
        return dtos;
    }
}
