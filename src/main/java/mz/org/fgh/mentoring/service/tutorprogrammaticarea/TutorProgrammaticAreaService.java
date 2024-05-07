package mz.org.fgh.mentoring.service.tutorprogrammaticarea;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.tutorProgrammaticArea.TutorProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.programaticarea.TutorProgrammaticAreaRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class TutorProgrammaticAreaService {

    private final TutorProgrammaticAreaRepository tutorProgrammaticAreaRepository;
    private final UserRepository userRepository;
    public TutorProgrammaticAreaService(TutorProgrammaticAreaRepository tutorProgrammaticAreaRepository, UserRepository userRepository) {
        this.tutorProgrammaticAreaRepository = tutorProgrammaticAreaRepository;
        this.userRepository = userRepository;
    }
    public List<TutorProgrammaticArea> fetchAllTutorProgrammaticAreas(Long tutorId){

        List<TutorProgrammaticArea> tutorProgrammaticAreas = this.tutorProgrammaticAreaRepository.getAllByTutorId(tutorId);

        return tutorProgrammaticAreas;
    }
    public TutorProgrammaticArea create(final TutorProgrammaticArea tutorProgrammaticArea, Long userId){
        User user = userRepository.findById(userId).get();
        tutorProgrammaticArea.setCreatedBy(user.getUuid());
        tutorProgrammaticArea.setUuid(UUID.randomUUID().toString());
        tutorProgrammaticArea.setCreatedAt(DateUtils.getCurrentDate());
        tutorProgrammaticArea.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        return this.tutorProgrammaticAreaRepository.save(tutorProgrammaticArea);
    }
    public TutorProgrammaticArea update(final TutorProgrammaticArea tutorProgrammaticArea, Long userId){
        TutorProgrammaticArea tutorProgrammaticAreaDB = findById(tutorProgrammaticArea.getId());
        User user = userRepository.findById(userId).get();
        tutorProgrammaticAreaDB.setUpdatedBy(user.getUuid());
        tutorProgrammaticAreaDB.setUpdatedAt(DateUtils.getCurrentDate());
        tutorProgrammaticAreaDB.setProgrammaticArea(tutorProgrammaticArea.getProgrammaticArea());
        tutorProgrammaticAreaDB.setTutor(tutorProgrammaticArea.getTutor());
        return this.tutorProgrammaticAreaRepository.update(tutorProgrammaticAreaDB);
    }

    public List<TutorProgrammaticAreaDTO> findAllTutorProgrammaticAreas() {
        List<TutorProgrammaticArea> tutList = (List<TutorProgrammaticArea>) this.tutorProgrammaticAreaRepository.findAll();
        List<TutorProgrammaticAreaDTO> programs = new ArrayList<TutorProgrammaticAreaDTO>();
        for (TutorProgrammaticArea tut: tutList) {
            TutorProgrammaticAreaDTO tutDTO = new TutorProgrammaticAreaDTO(tut);
            programs.add(tutDTO);
        }
        return programs;
    }
    public TutorProgrammaticArea findById(final Long id){
        return this.tutorProgrammaticAreaRepository.findById(id).get();
    }

    public TutorProgrammaticArea updateLifeCycleStatus(TutorProgrammaticArea tutorProgrammaticArea, Long userId) {
        User user = this.userRepository.fetchByUserId(userId);
        Optional<TutorProgrammaticArea> tutorProgrammaticAreaRepositoryByUuid =  this.tutorProgrammaticAreaRepository.findByUuid(tutorProgrammaticArea.getUuid());
        if (tutorProgrammaticAreaRepositoryByUuid.isPresent()) {
            tutorProgrammaticAreaRepositoryByUuid.get().setLifeCycleStatus(tutorProgrammaticArea.getLifeCycleStatus());
            tutorProgrammaticAreaRepositoryByUuid.get().setUpdatedBy(user.getUuid());
            tutorProgrammaticAreaRepositoryByUuid.get().setUpdatedAt(DateUtils.getCurrentDate());
            this.tutorProgrammaticAreaRepository.update(tutorProgrammaticAreaRepositoryByUuid.get());
            return tutorProgrammaticAreaRepositoryByUuid.get();
        }
        return null;
    }
}
