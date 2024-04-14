package mz.org.fgh.mentoring.repository.question;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.QuestionsCategory;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.List;
import java.util.Set;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    @Override
    List<Question> findAll();

    @Query("FROM Question q " +
            "INNER JOIN FETCH q.questionsCategory qc " +
            "WHERE q.lifeCycleStatus = :lifeCycleStatus")
    List<Question> getAllQuestions(LifeCycleStatus lifeCycleStatus);

    @Query("FROM Question q " +
            "INNER JOIN FETCH q.questionsCategory qc " +
            "WHERE q.lifeCycleStatus = :lifeCycleStatus " )
    //" AND q.id IN (:ids) "
    List<Question> getQuestionsByIds(List<Long> ids, LifeCycleStatus lifeCycleStatus);

    List<Long> search(final String code, final String question, final QuestionsCategory questionsCategory);

}
