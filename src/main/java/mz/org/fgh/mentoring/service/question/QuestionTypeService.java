package mz.org.fgh.mentoring.service.question;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.question.QuestionType;
import mz.org.fgh.mentoring.repository.question.QuestionTypeRepository;

import java.util.List;

@Singleton
public class QuestionTypeService {

    @Inject
    QuestionTypeRepository questionTypeRepository;

    public List<QuestionType> findAll(){return this.questionTypeRepository.findAll();
    }

    public QuestionType getByCode(String code) {
        return questionTypeRepository.getByCode(code);
    }
}
