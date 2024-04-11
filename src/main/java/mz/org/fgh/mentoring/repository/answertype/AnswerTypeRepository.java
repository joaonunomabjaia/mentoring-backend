package mz.org.fgh.mentoring.repository.answertype;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.answertype.AnswerType;

import java.util.List;

@Repository
public interface AnswerTypeRepository extends CrudRepository<AnswerType, Long> {

    List<AnswerType> findAll();
}
