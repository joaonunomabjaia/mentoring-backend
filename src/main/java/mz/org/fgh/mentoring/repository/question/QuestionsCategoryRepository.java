package mz.org.fgh.mentoring.repository.question;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.question.QuestionCategory;
import mz.org.fgh.mentoring.entity.question.QuestionsCategory;

import java.util.List;

@Repository
public interface QuestionsCategoryRepository  extends CrudRepository<QuestionCategory, Long> {
    List<QuestionCategory> findAll();
}
