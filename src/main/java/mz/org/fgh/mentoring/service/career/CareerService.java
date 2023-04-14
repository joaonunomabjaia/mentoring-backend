package mz.org.fgh.mentoring.service.career;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.career.Career;
import mz.org.fgh.mentoring.repository.tutor.CareerRepository;

import java.util.List;
import java.util.Optional;

@Singleton
public class CareerService {

    @Inject
    CareerRepository careerRepository;

    public List<Career> findAll(){
        return this.careerRepository.findAll();
    }

    public Optional<Career> findById(Long id){
        return this.careerRepository.findById(id);
    }

    public Career create(Career career){
        return this.careerRepository.save(career);
    }
}
