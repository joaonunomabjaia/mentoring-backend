package mz.org.fgh.mentoring.service.tutor;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutor.TutorLocation;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.tutor.TutorLocationRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Singleton
public class TutorLocationService {

    private final TutorLocationRepository tutorLocationRepository;

    public TutorLocationService(TutorLocationRepository tutorLocationRepository){
        this.tutorLocationRepository = tutorLocationRepository;
    }

    public TutorLocation createTutorLocation(TutorLocation tutorLocation){
        if(tutorLocation.getLocation() == null && tutorLocation.getTutor() == null){
            throw new MentoringBusinessException("Fields 'TUTOR' and 'LOCATION' are required.");
        }
        return tutorLocationRepository.save(tutorLocation);
    }

    public TutorLocation findTutorLocationById(@NotNull Long id){
        Optional<TutorLocation> optionalTutorLocation = tutorLocationRepository.findById(id);
        if(optionalTutorLocation == null){
            throw new MentoringBusinessException("Tutor Location with ID: "+id+" was not found.");
        }
        return optionalTutorLocation.get();
    }

    public List<TutorLocation> findAllTutorLocations(){
        return tutorLocationRepository.findAll();
    }

    public List<TutorLocation> findTutorLocationsByTutor(final Tutor tutor){
        return tutorLocationRepository.findByTutor(tutor);
    }
}
