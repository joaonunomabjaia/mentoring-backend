package mz.org.fgh.mentoring.repository.form;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


public interface FormQuestionRepository extends PageableRepository<FormQuestion, Long> {

    List<FormQuestion> findAll();

    Optional<FormQuestion> findById(@NotNull Long id);

    @Query(value = "FROM FormQuestion fq " +
            "INNER JOIN FETCH fq.form f " +
            "INNER JOIN FETCH fq.question q " +
            "INNER JOIN FETCH fq.evaluationType et " +
            "INNER JOIN FETCH fq.responseType rt " +
            "INNER JOIN FETCH q.program " +
            "WHERE f.id = :formId AND fq.lifeCycleStatus = 'ACTIVE'",

            countQuery = "SELECT COUNT(fq) FROM FormQuestion fq " +
                    "WHERE fq.form.id = :formId AND fq.lifeCycleStatus = 'ACTIVE'")
    Page<FormQuestion> fetchByForm(Long formId, Pageable pageable);


    @Query("FROM FormQuestion fq " +
            "INNER JOIN FETCH fq.form f " +
            "INNER JOIN FETCH fq.question q " +
            "INNER JOIN FETCH fq.evaluationType et " +
            "INNER JOIN FETCH fq.responseType rt " +
            "INNER JOIN FETCH q.program " +
            "WHERE f.id = :formId AND fq.lifeCycleStatus = 'ACTIVE'")
    List<FormQuestion> fetchByForm(Long formId);


    List<FormQuestion> fetchByTutor(final Tutor tutor, final LifeCycleStatus lifeCycleStatus);

    @Query("SELECT fq FROM FormQuestion fq INNER JOIN FETCH fq.form f INNER JOIN FETCH f.programmaticArea INNER JOIN FETCH fq.question q INNER JOIN FETCH q.program WHERE f.code = :formCode AND fq.lifeCycleStatus = :lifeCycleStatus")
    List<FormQuestion> fetchByFormCode(String formCode, LifeCycleStatus lifeCycleStatus);

    @Query("FROM FormQuestion fq " +
            "INNER JOIN FETCH fq.form f " +
            "INNER JOIN FETCH fq.question q " +
            "INNER JOIN FETCH fq.evaluationType et " +
            "INNER JOIN FETCH fq.responseType rt " +
            "INNER JOIN FETCH q.program " +
            "WHERE f.uuid IN (:formsUuids)")
    List<FormQuestion> fetchByFormsUuids(List<String> formsUuids, Integer offset, Integer limit);

    @Query("FROM FormQuestion fq " +
            "INNER JOIN FETCH fq.form f " +
            "INNER JOIN FETCH fq.question q " +
            "INNER JOIN FETCH fq.evaluationType et " +
            "INNER JOIN FETCH fq.responseType rt " +
            "INNER JOIN FETCH q.program " +
            "WHERE f.uuid IN (:formsUuids)")
    List<FormQuestion> findByFormsUuids(List<String> formsUuids, Pageable pageable);

    Optional<FormQuestion> findByUuid(String uuid);
}
