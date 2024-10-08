package mz.org.fgh.mentoring.repository.form;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import mz.org.fgh.mentoring.entity.form.FormSection;

import java.util.Optional;

@Repository
public interface FormSectionRepository extends JpaRepository<FormSection, Long> {
    // Custom query methods if needed can be added here

    Optional<FormSection> findByUuid(String uuid);

}