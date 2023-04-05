package mz.org.fgh.mentoring.service.tutor;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;

import java.util.List;

@Singleton
public class TutorService {

    TutorRepository tutorRepository;

    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public List<Tutor> findAll() {
        return this.tutorRepository.findAll();
    }

    public Tutor create(Tutor tutor) {
        return this.tutorRepository.save(tutor);
    }
}
