package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormQuestionRepository extends CrudRepository<FormQuestion, Long> {

    List<FormQuestion> findAll();

    Optional<FormQuestion> findById(@NotNull Long id);
}
