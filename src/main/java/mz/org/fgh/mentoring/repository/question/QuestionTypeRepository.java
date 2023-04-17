package mz.org.fgh.mentoring.repository.question;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.question.QuestionType;

@Repository
public interface QuestionTypeRepository extends CrudRepository<QuestionType, Long> {

    QuestionType getByCode(String code);
}
