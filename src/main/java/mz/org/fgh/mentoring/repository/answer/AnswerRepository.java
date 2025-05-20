package mz.org.fgh.mentoring.repository.answer;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
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

    List<Answer> findByMentorship(Mentorship mentorship);

    @Query("SELECT a FROM Answer a " +
            "JOIN FETCH a.question q " +
            "JOIN FETCH a.mentorship m " +
            "JOIN FETCH m.tutored t " +
            "JOIN FETCH t.employee e " +
            "WHERE m.tutored.id = :menteeId AND a.value = 'NAO' AND m.id = 400378 " +
            "ORDER BY a.createdAt DESC")
    List<Answer> findWeakAnswersByMenteeId(Long menteeId);

    @Query("SELECT a FROM Answer a " +
            "JOIN FETCH a.question q " +
            "JOIN FETCH a.mentorship m " +
            "JOIN FETCH m.session s " +
            "JOIN FETCH m.form f " +
            "JOIN FETCH f.programmaticArea pa " +
            "JOIN FETCH pa.program pr " +
            "JOIN FETCH m.tutored t " +
            "JOIN FETCH t.employee e " +
            "WHERE a.value = 'NAO' AND m.id = :mentorshipId " +
            "ORDER BY a.createdAt DESC")
    List<Answer> findWeakAnswersByMentorshipId(Long mentorshipId);

}
