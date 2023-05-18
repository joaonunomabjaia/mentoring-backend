package mz.org.fgh.mentoring.repository.question;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.question.QuestionType;

import java.util.List;

@Repository
public interface QuestionTypeRepository extends CrudRepository<QuestionType, Long> {

    List<QuestionType> findAll();
    QuestionType getByCode(String code);
}
