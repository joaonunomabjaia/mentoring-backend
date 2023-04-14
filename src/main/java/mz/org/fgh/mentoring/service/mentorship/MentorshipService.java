package mz.org.fgh.mentoring.service.mentorship;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.mentorship.MentorshipRepository;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Singleton
public class MentorshipService {

    private final MentorshipRepository mentorshipRepository;

    public MentorshipService(MentorshipRepository mentorshipRepository){
        this.mentorshipRepository = mentorshipRepository;
    }

    public Mentorship createMentorship(Mentorship mentorship){
        if(StringUtils.isEmpty(mentorship.getCode()) && mentorship.getHealthFacility() == null && mentorship.getTutor() == null && mentorship.getTutored() == null){
            throw new MentoringBusinessException("Fields 'CODE', 'HEALTH FACILITY', 'TUTOR' and 'TUTORED' are required.");
        }
        return mentorshipRepository.save(mentorship);
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
}
