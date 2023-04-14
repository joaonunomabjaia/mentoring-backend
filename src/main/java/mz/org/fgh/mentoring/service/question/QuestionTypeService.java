package mz.org.fgh.mentoring.service.question;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.question.QuestionType;
import mz.org.fgh.mentoring.repository.question.QuestionTypeRepository;

@Singleton
public class QuestionTypeService {

    @Inject
    QuestionTypeRepository questionTypeRepository;

    public QuestionType getByCode(String code) {
        return questionTypeRepository.getByCode(code);
    }
}
