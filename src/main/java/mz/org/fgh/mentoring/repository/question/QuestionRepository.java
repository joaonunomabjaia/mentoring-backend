package mz.org.fgh.mentoring.repository.question;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.List;
import java.util.Set;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
    @Override
    List<Question> findAll();

    @Query("FROM Question q " +
            "INNER JOIN FETCH q.questionType qt " +
            "INNER JOIN FETCH q.questionsCategory qc " +
            "INNER JOIN FETCH q.responseType rt " +
            "WHERE q.lifeCycleStatus = :lifeCycleStatus")
    List<Question> getAllQuestions(LifeCycleStatus lifeCycleStatus);

}
