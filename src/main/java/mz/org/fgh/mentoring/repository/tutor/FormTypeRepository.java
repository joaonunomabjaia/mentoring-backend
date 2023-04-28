package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.form.FormType;

import java.util.List;

@Repository
public interface FormTypeRepository extends CrudRepository<FormType, Long> {

    List<FormType> findAll();
}
