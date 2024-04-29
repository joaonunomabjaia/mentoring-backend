package mz.org.fgh.mentoring.service.mentorship;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.mentorship.IterationTypeDTO;
import mz.org.fgh.mentoring.entity.mentorship.IterationType;
import mz.org.fgh.mentoring.repository.mentorship.IterationTypeRepository;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class IterationTypeService {

    @Inject
    IterationTypeRepository iterationTypeRepository;

    public List<IterationTypeDTO> findAll(){
        List<IterationTypeDTO> iterationTypeDTOS = new ArrayList<>();
        List<IterationType> iterationTypes = this.iterationTypeRepository.findAll();
        for (IterationType iterationType : iterationTypes) {
            IterationTypeDTO iterationTypeDTO = new IterationTypeDTO(iterationType);
            iterationTypeDTOS.add(iterationTypeDTO);
        }
        return iterationTypeDTOS;
    }

    public IterationTypeDTO getByCode(String code) {
        IterationType iterationType = iterationTypeRepository.getByCode(code);
        IterationTypeDTO iterationTypeDTO = new IterationTypeDTO(iterationType);
        return iterationTypeDTO;
    }
}
