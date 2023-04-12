package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.question.QuestionType;

public interface QuestionTypeRepository extends CrudRepository<QuestionType, Long> {

    QuestionType getByCode(String code);
}
