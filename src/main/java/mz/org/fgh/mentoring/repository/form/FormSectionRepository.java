package mz.org.fgh.mentoring.repository.form;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.form.FormSection;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormSectionRepository extends JpaRepository<FormSection, Long> {
    // Custom query methods if needed can be added here

    Optional<FormSection> findByUuid(String uuid);

    List<FormSection> findByForm(Form form);

    @Query("SELECT fs FROM FormSection fs JOIN FETCH fs.formSectionQuestions WHERE fs.form.id = :formId")
    List<FormSection> findByFormWithQuestions(Long formId);


}