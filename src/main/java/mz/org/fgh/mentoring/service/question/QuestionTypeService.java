package mz.org.fgh.mentoring.service.question;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.question.QuestionTypeDTO;
import mz.org.fgh.mentoring.entity.question.QuestionType;
import mz.org.fgh.mentoring.repository.question.QuestionTypeRepository;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class QuestionTypeService {

    @Inject
    QuestionTypeRepository questionTypeRepository;

    public List<QuestionTypeDTO> findAll(){
        List<QuestionTypeDTO> questionTypeDTOS = new ArrayList<>();
        List<QuestionType> questionTypes = this.questionTypeRepository.findAll();
        for (QuestionType questionType:questionTypes) {
            QuestionTypeDTO questionTypeDTO = new QuestionTypeDTO(questionType);
            questionTypeDTOS.add(questionTypeDTO);
        }
        return questionTypeDTOS;
    }

    public QuestionTypeDTO getByCode(String code) {
        QuestionType questionType = questionTypeRepository.getByCode(code);
        QuestionTypeDTO questionTypeDTO = new QuestionTypeDTO(questionType);
        return questionTypeDTO;
    }
}
