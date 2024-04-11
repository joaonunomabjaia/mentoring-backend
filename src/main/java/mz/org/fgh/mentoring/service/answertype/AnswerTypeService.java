package mz.org.fgh.mentoring.service.answertype;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.answertype.AnswerType;
import mz.org.fgh.mentoring.repository.answertype.AnswerTypeRepository;

import java.util.List;

@Singleton
public class AnswerTypeService {

    private final AnswerTypeRepository answerTypeRepository;

    public AnswerTypeService(AnswerTypeRepository answerTypeRepository) {
        this.answerTypeRepository = answerTypeRepository;
    }

    public List<AnswerType> getAll() {
        return this.answerTypeRepository.findAll();
    }
}
