package mz.org.fgh.mentoring.service.tutorprogrammaticarea;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.repository.programaticarea.TutorProgrammaticAreaRepository;

@Singleton
public class TutorProgrammaticAreaService {

    private final TutorProgrammaticAreaRepository tutorProgrammaticAreaRepository;
    public TutorProgrammaticAreaService(TutorProgrammaticAreaRepository tutorProgrammaticAreaRepository) {
        this.tutorProgrammaticAreaRepository = tutorProgrammaticAreaRepository;
    }
    public TutorProgrammaticArea create(final TutorProgrammaticArea tutorProgrammaticArea){
      return this.tutorProgrammaticAreaRepository.save(tutorProgrammaticArea);
    }
    public TutorProgrammaticArea update(final TutorProgrammaticArea tutorProgrammaticArea){
        return this.tutorProgrammaticAreaRepository.update(tutorProgrammaticArea);
    }
}
