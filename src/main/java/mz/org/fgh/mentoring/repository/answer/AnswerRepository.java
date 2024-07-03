package mz.org.fgh.mentoring.repository.answer;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {

    List<Answer> findAll();

    Optional<Answer> findById(@NotNull Long id);

    @Query("FROM Answer a INNER JOIN FETCH a.question q INNER JOIN FETCH a.mentorship m INNER JOIN FETCH a.form f WHERE m.uuid = :mentorshipUuid AND a.lifeCycleStatus = :lifeCycleStatus")
    List<Answer> fetchByMentorshipUuid(final String mentorshipUuid, final LifeCycleStatus lifeCycleStatus);

    @Query("Select a FROM Answer a join fetch a.question q WHERE q.id = :questionId")
    List<Answer> getByQuestionId(Long questionId);
}
