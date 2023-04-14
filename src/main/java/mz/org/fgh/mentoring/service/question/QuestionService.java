package mz.org.fgh.mentoring.service.question;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.repository.question.QuestionRepository;

import java.util.List;

@Singleton
public class QuestionService {

    @Inject
    private QuestionRepository questionRepository;

    public List<Question> getQuestionsByFormCode(String formCode) {
        return null;
    }
}
