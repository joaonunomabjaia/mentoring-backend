package mz.org.fgh.mentoring.service.career;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.career.Career;
import mz.org.fgh.mentoring.entity.career.CareerType;
import mz.org.fgh.mentoring.repository.career.CareerRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.List;
import java.util.Optional;

@Singleton
public class CareerService {

    private final CareerRepository careerRepository;
    public CareerService(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }

    public List<Career> findAll(){
        return this.careerRepository.findAll();
    }

    public Optional<Career> findById(Long id){
        return this.careerRepository.findById(id);
    }

    public List<Career> findAllCareerTypes(final CareerType careerType){
        return this.careerRepository.findByCarrerTyp(careerType, LifeCycleStatus.ACTIVE);
    }

    public Career create(Career career){
        return this.careerRepository.save(career);
    }

    public List<Career> findCareerWithLimit(Long limit, Long offset){
        return this.careerRepository.findCareerWithLimit(limit, offset);
    }
}
