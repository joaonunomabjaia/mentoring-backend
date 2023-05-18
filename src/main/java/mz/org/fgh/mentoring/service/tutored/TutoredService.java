package mz.org.fgh.mentoring.service.tutored;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.repository.tutored.TutoredRepository;

import java.util.List;

@Singleton
public class TutoredService {

    private final TutoredRepository tutoredRepository;
    public TutoredService(TutoredRepository tutoredRepository) {
        this.tutoredRepository = tutoredRepository;
    }

    public List<Tutored> findAll(){
        return this.tutoredRepository.findAll();
    }
}
