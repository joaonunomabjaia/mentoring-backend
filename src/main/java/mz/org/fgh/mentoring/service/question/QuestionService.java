package mz.org.fgh.mentoring.service.question;

import io.micronaut.core.annotation.Creator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.QuestionCategory;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.question.QuestionRepository;
import mz.org.fgh.mentoring.repository.question.QuestionsCategoryRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class QuestionService {
    @Inject
    private QuestionRepository questionRepository;
    @Inject
    private QuestionsCategoryRepository questionCategoryRepository;
    @Inject
    private UserRepository userRepository;
    @Creator
    public QuestionService(){}

    public QuestionService(QuestionRepository questionRepository, QuestionsCategoryRepository questionsCategoryRepository) {
        this.questionRepository = questionRepository;
        this.questionCategoryRepository = questionsCategoryRepository;
    }


    public QuestionService(QuestionRepository questionRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
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
        QuestionCategory questionCategory = null;
        if(categoryId != null && categoryId > 0) {
            questionCategory = questionCategoryRepository.findById(categoryId).get();
        }
        List<Question> questions = questionRepository.search(code, description, questionCategory);
        List<QuestionDTO> dtos = new ArrayList<>();
        for (Question question : questions) {
            QuestionDTO dto = new QuestionDTO(question);
            dtos.add(dto);
        }
        return dtos;
    }
    public List<QuestionDTO> findAllQuestions() {
        List<Question> questionList = this.questionRepository.findAll();

        try {
            return Utilities.parseList(questionList,QuestionDTO.class);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Question create(Question question, Long userId) {
        User user = userRepository.findById(userId).get();
        question.setCreatedBy(user.getUuid());
        question.setUuid(UUID.randomUUID().toString());
        question.setCreatedAt(DateUtils.getCurrentDate());
        question.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        question.setQuestion(question.getQuestion());
        question.setQuestionCategory(question.getQuestionCategory());

        return this.questionRepository.save(question);
    }
    public Optional<Question> findById(final Long id){
        return this.questionRepository.findById(id);
    }
    @Transactional
    public Question update(Question question, Long userId) {
        User user = userRepository.findById(userId).get();
        question.setUpdatedBy(user.getUuid());
        question.setUpdatedAt(DateUtils.getCurrentDate());
        question.setQuestion(question.getQuestion());
        question.setQuestionCategory(question.getQuestionCategory());

        return this.questionRepository.update(question);
    }
}
