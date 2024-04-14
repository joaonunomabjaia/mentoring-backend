package mz.org.fgh.mentoring.service.question;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.question.EvaluationTypeDTO;
import mz.org.fgh.mentoring.entity.question.EvaluationType;
import mz.org.fgh.mentoring.repository.question.EvaluationTypeRepository;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class EvaluationTypeService {

    @Inject
    EvaluationTypeRepository evaluationTypeRepository;

    public List<EvaluationTypeDTO> findAll(){
        List<EvaluationTypeDTO> evaluationTypeDTOS = new ArrayList<>();
        List<EvaluationType> evaluationTypes = this.evaluationTypeRepository.findAll();
        for (EvaluationType evaluationType : evaluationTypes) {
            EvaluationTypeDTO evaluationTypeDTO = new EvaluationTypeDTO(evaluationType);
            evaluationTypeDTOS.add(evaluationTypeDTO);
        }
        return evaluationTypeDTOS;
    }

    public EvaluationTypeDTO getByCode(String code) {
        EvaluationType evaluationType = evaluationTypeRepository.getByCode(code);
        EvaluationTypeDTO evaluationTypeDTO = new EvaluationTypeDTO(evaluationType);
        return evaluationTypeDTO;
    }
}
