package mz.org.fgh.mentoring.repository.form;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;
import mz.org.fgh.mentoring.entity.formQuestion.FormSectionQuestion;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormSectionQuestionRepository extends PageableRepository<FormSectionQuestion, Long> {

    List<FormSectionQuestion> findAll();

    Optional<FormSectionQuestion> findById(@NotNull Long id);

    @Query(value = "FROM FormSectionQuestion fq " +
            "INNER JOIN FETCH fq.formSection fs " +
            "INNER JOIN FETCH fs.form f " +
            "INNER JOIN FETCH fq.question q " +
            "INNER JOIN FETCH fq.evaluationType et " +
            "INNER JOIN FETCH fq.responseType rt " +
            "INNER JOIN FETCH q.program " +
            "WHERE f.id = :formId AND fq.lifeCycleStatus = 'ACTIVE'",

            countQuery = "SELECT COUNT(fq) FROM FormSectionQuestion fq " +
                    "WHERE fq.form.id = :formId AND fq.lifeCycleStatus = 'ACTIVE'")
    Page<FormSectionQuestion> fetchByForm(Long formId, Pageable pageable);


    @Query("FROM FormSectionQuestion fq " +
            "INNER JOIN FETCH fq.formSection fs " +
            "INNER JOIN FETCH fs.form f " +
            "INNER JOIN FETCH fq.question q " +
            "INNER JOIN FETCH fq.evaluationType et " +
            "INNER JOIN FETCH fq.responseType rt " +
            "INNER JOIN FETCH q.program " +
            "WHERE f.id = :formId AND fq.lifeCycleStatus = 'ACTIVE'")
    List<FormSectionQuestion> fetchByForm(Long formId);


    @Query("SELECT fq FROM FormSectionQuestion fq INNER JOIN FETCH fq.form f INNER JOIN FETCH f.programmaticArea INNER JOIN FETCH fq.question q INNER JOIN FETCH q.program WHERE f.code = :formCode AND fq.lifeCycleStatus = :lifeCycleStatus")
    List<FormSectionQuestion> fetchByFormCode(String formCode, LifeCycleStatus lifeCycleStatus);

    @Query("FROM FormSectionQuestion fq " +
            "INNER JOIN FETCH fq.formSection fs " +
            "INNER JOIN FETCH fs.form f " +
            "INNER JOIN FETCH fq.question q " +
            "INNER JOIN FETCH fq.evaluationType et " +
            "INNER JOIN FETCH fq.responseType rt " +
            "INNER JOIN FETCH q.program " +
            "WHERE f.uuid IN (:formsUuids)")
    List<FormSectionQuestion> fetchByFormsUuids(List<String> formsUuids, Integer offset, Integer limit);

    @Query("FROM FormSectionQuestion fq " +
            "INNER JOIN FETCH fq.formSection fs " +
            "INNER JOIN FETCH fs.form f " +
            "INNER JOIN FETCH fq.question q " +
            "INNER JOIN FETCH fq.evaluationType et " +
            "INNER JOIN FETCH fq.responseType rt " +
            "INNER JOIN FETCH q.program " +
            "WHERE f.uuid IN (:formsUuids)")
    List<FormSectionQuestion> findByFormsUuids(List<String> formsUuids, Pageable pageable);

    Optional<FormSectionQuestion> findByUuid(String uuid);
}
