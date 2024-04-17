package mz.org.fgh.mentoring.service.question;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.QuestionsCategory;
import mz.org.fgh.mentoring.repository.question.QuestionRepository;
import mz.org.fgh.mentoring.repository.question.QuestionsCategoryRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class QuestionService {

    private QuestionRepository questionRepository;

    private QuestionsCategoryRepository questionsCategoryRepository;

    public QuestionService(QuestionRepository questionRepository, QuestionsCategoryRepository questionsCategoryRepository) {
        this.questionRepository = questionRepository;
        this.questionsCategoryRepository = questionsCategoryRepository;
    }

    public List<Question> getQuestionsByFormCode(String formCode) {
        return null;
    }

    public List<QuestionDTO> getAllQuestions() {
        List<Question> questions = questionRepository.getAllQuestions(LifeCycleStatus.ACTIVE);
        List<QuestionDTO> dtos = new ArrayList<>();
        for (Question question : questions) {
            QuestionDTO dto = new QuestionDTO(question);
            dtos.add(dto);
        }
        return dtos;
    }

    public  List<QuestionDTO> search(final String code, final String description, final Long categoryId) {
        QuestionsCategory questionsCategory = null;
        if(categoryId!=null) {
            questionsCategory = questionsCategoryRepository.findById(categoryId).get();
        }
        List<Long> ids = questionRepository.search(code, description, questionsCategory);
        List<Question> questions = questionRepository.getQuestionsByIds(ids, LifeCycleStatus.ACTIVE);
        try {
            return Utilities.parseList(questions, QuestionDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
