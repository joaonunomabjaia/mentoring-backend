package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.answer.Answer;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {

    List<Answer> findAll();

    Optional<Answer> findById(@NotNull Long id);
}
