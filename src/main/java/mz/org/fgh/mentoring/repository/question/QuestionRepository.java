package mz.org.fgh.mentoring.repository.question;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.QuestionCategory;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    @Override
    List<Question> findAll();

    @Query("FROM Question q " +
            "INNER JOIN FETCH q.questionCategory qc " +
            "WHERE q.lifeCycleStatus = :lifeCycleStatus")
    List<Question> getAllQuestions(LifeCycleStatus lifeCycleStatus);

    @Query("FROM Question q " +
            "INNER JOIN FETCH q.questionCategory qc " +
            "WHERE q.lifeCycleStatus = :lifeCycleStatus " +
             " AND q.id IN (:ids) ")
    List<Question> getQuestionsByIds(List<Long> ids, LifeCycleStatus lifeCycleStatus);

    List<Question> search(final String code, final String question, final QuestionCategory questionsCategory);

}
