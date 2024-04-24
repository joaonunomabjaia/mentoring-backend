package mz.org.fgh.mentoring.service.career;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.career.CareerTypeDTO;
import mz.org.fgh.mentoring.entity.career.CareerType;
import mz.org.fgh.mentoring.repository.career.CareerTypeRepository;
import mz.org.fgh.mentoring.util.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class CareerTypeService {


    private final CareerTypeRepository careerTypeRepository;

    public CareerTypeService(CareerTypeRepository careerTypeRepository) {
        this.careerTypeRepository = careerTypeRepository;
    }

    public List<CareerTypeDTO> findAllCareerTypes(Long limit, Long offset){
        try {
        List<CareerType> careerTypes = new ArrayList<>();
        if(limit!=null && offset!=null && limit > 0){
           careerTypes = this.careerTypeRepository.findCareerTypeWithLimit(limit, offset);
        }else{
            careerTypes = this.careerTypeRepository.findAll();
        }
        return  Utilities.parseList(careerTypes, CareerTypeDTO.class);
         } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                      throw new RuntimeException(e);
          }
    }
}
