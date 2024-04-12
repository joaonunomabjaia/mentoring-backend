package mz.org.fgh.mentoring.repository.form;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


public interface FormQuestionRepository extends CrudRepository<FormQuestion, Long> {

    List<FormQuestion> findAll();

    Optional<FormQuestion> findById(@NotNull Long id);

    @Query("FROM FormQuestion fq " +
            "INNER JOIN FETCH fq.form f " +
            "INNER JOIN FETCH fq.question q " +
            "INNER JOIN FETCH fq.evaluationType et " +
            "INNER JOIN FETCH fq.responseType rt " +
            "INNER JOIN FETCH q.questionsCategory " +
            "WHERE f.id = :formId AND fq.lifeCycleStatus = :lifeCycleStatus")
    List<FormQuestion> fetchByForm(Long formId, LifeCycleStatus lifeCycleStatus);

    List<FormQuestion> fetchByTutor(final Tutor tutor, final LifeCycleStatus lifeCycleStatus);

    @Query("SELECT fq FROM FormQuestion fq INNER JOIN FETCH fq.form f INNER JOIN FETCH f.programmaticArea INNER JOIN FETCH fq.question q INNER JOIN FETCH q.questionsCategory WHERE f.code = :formCode AND fq.lifeCycleStatus = :lifeCycleStatus")
    List<FormQuestion> fetchByFormCode(String formCode, LifeCycleStatus lifeCycleStatus);
}
