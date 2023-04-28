package mz.org.fgh.mentoring.service.career;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.career.CareerType;
import mz.org.fgh.mentoring.repository.tutor.CareerTypeRepository;

import java.util.List;

@Singleton
public class CareeTypeService {

    @Inject
    private CareerTypeRepository careerTypeRepository;

    public List<CareerType> findAllCareeType(){
       return this.careerTypeRepository.findAll();
    }
}
