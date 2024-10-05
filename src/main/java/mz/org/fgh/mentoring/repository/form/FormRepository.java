package mz.org.fgh.mentoring.repository.form;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface FormRepository extends CrudRepository<Form, Long> {

    @Override
    List<Form> findAll();

    Page<Form> findAll(Pageable pageable);

    Optional<Form> findById(@NotNull Long id);

    Optional<Form> findByUuid(@NotNull String uuid);

    Form findByCode(String code);

    @Query("select f FROM Answer a INNER JOIN a.form f INNER JOIN a.question q INNER JOIN FETCH f.programmaticArea WHERE q.uuid IN (:questionUuids) AND f.lifeCycleStatus = :lifeCycleStatus")
    List<Form> findSampleIndicatorForms(List<String> questionUuids ,LifeCycleStatus lifeCycleStatus);

    @Query("select f from Form f " +
            "INNER JOIN FETCH f.programmaticArea pa " +
            "INNER JOIN FETCH pa.program p " +
            "INNER JOIN FETCH f.partner pt " +
            "where f.code like concat(concat('%',:code) ,'%') and f.name like concat(concat('%',:name),'%') " +
            "and pa.code like concat(concat('%',:programmaticAreaCode) ,'%') and p.uuid like concat(concat('%',:program) ,'%') ")
    List<Form> findBySelectedFilter(final String code, final String name, final String programmaticAreaCode, String program);

    @Query("select f FROM Form f INNER JOIN FETCH f.programmaticArea pa INNER JOIN FETCH f.formType  where pa.uuid = :programmaticAreaUuid")
    List<Form> findFormByProgrammaticAreaUuid(final String programmaticAreaUuid);

    @Query("select f FROM Form f INNER JOIN FETCH f.programmaticArea pa  where pa.id = :programmaticAreaId")
    List<Form> findFormByProgrammaticAreaId(final Long programmaticAreaId);

    @Query(value = "select * from forms limit :lim offset :of ", nativeQuery = true)
    List<Form> findFormWithLimit(long lim, long of);

    List<Form> search(final String code, final String name, final String programmaticArea);

    @Query("select f from Form f " +
            "INNER JOIN FETCH f.programmaticArea pa " +
            "INNER JOIN FETCH pa.program p " +
            "INNER JOIN FETCH f.partner pt " +
            "where pt.id = :partnerId ")
    List<Form> fetchByPartnerId(final Long partnerId);


    List<Form> getAllOfTutor(final Tutor tutor);

    @Query("SELECT f FROM Form f " +
            "WHERE f.programmaticArea.program = :program " +
            "ORDER BY f.createdAt DESC")
    Optional<Form> findTopByProgramOrderByCreatedAtDesc(Program program);

}
