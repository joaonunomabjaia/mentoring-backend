package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends CrudRepository<Form, Long> {

    List<Form> findAll();
    Optional<Form> findById(@NotNull Long id);

    Optional<Form> findByUuid(String uuid);
    Form findByCode(String code);

    @Query("select f FROM Answer a INNER JOIN a.form f INNER JOIN a.question q INNER JOIN FETCH f.programmaticArea WHERE q.uuid IN (:questionUuids) AND f.lifeCycleStatus = :lifeCycleStatus")
    List<Form> findSampleIndicatorForms(List<String> questionUuids ,LifeCycleStatus lifeCycleStatus);

    @Query("select f from Form f INNER JOIN FETCH f.programmaticArea pa INNER JOIN FETCH f.partiner p where f.code like concat(concat('%',:code) ,'%') and f.name like concat(concat('%',:name),'%') and pa.code like concat(concat('%',:programmaticAreaCode) ,'%') and f.lifeCycleStatus = :lifeCycleStatus and p.uuid like concat(concat('%',:partnerUUID),'%')")
    List<Form> findBySelectedFilter(final String code, final String name, final String programmaticAreaCode, final LifeCycleStatus lifeCycleStatus, final String partnerUUID);

    @Query("select f FROM Form f INNER JOIN FETCH f.programmaticArea pa INNER JOIN FETCH f.formType  where pa.uuid = :programaticaAreaUuid")
    List<Form> findFormByProgrammaticAreaUuid(final String programaticaAreaUuid);
}
