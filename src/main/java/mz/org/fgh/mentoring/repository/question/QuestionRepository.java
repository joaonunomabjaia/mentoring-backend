package mz.org.fgh.mentoring.repository.question;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.question.Question;

import java.util.List;
import java.util.Set;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {

}
