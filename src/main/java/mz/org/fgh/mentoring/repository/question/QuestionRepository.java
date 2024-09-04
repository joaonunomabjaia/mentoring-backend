package mz.org.fgh.mentoring.repository.question;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("FROM Question q " +
            "INNER JOIN FETCH q.questionCategory qc " +
            "WHERE q.lifeCycleStatus = :lifeCycleStatus")
    List<Question> getAllQuestions(LifeCycleStatus lifeCycleStatus);

    @Query("FROM Question q " +
            "INNER JOIN FETCH q.questionCategory qc " +
            "WHERE q.lifeCycleStatus = :lifeCycleStatus " +
             " AND q.id IN (:ids) ")
    List<Question> getQuestionsByIds(List<Long> ids, LifeCycleStatus lifeCycleStatus);

    @Query(value = "SELECT q FROM Question q INNER JOIN q.questionCategory qc WHERE (:code IS NULL OR q.code LIKE CONCAT('%', :code, '%')) AND (:question IS NULL OR q.question LIKE CONCAT('%', :question, '%')) AND (:questionsCategoryId IS NULL OR q.id = :questionsCategoryId)",
            countQuery = "SELECT COUNT(q) FROM Question q INNER JOIN q.questionCategory qc WHERE (:code IS NULL OR q.code LIKE CONCAT('%', :code, '%')) AND (:question IS NULL OR q.question LIKE CONCAT('%', :question, '%')) AND (:questionsCategoryId IS NULL OR q.id = :questionsCategoryId)")
    Page<Question> search(final String code, final String question, final Long questionsCategoryId, Pageable pageable);

    Optional<Question> findByUuid(String uuid);

    @Query(value = "select * from questions where LIFE_CYCLE_STATUS = 'ACTIVE' ", nativeQuery = true)
    List<Question> getByPageAndSize(Pageable pageable);

}
