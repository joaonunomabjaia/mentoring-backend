package mz.org.fgh.mentoring.repository.question;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("FROM Question q " +
            "INNER JOIN FETCH q.program qc " +
            "WHERE q.lifeCycleStatus = :lifeCycleStatus")
    List<Question> getAllQuestions(LifeCycleStatus lifeCycleStatus);

    @Query("FROM Question q " +
            "INNER JOIN FETCH q.program qc " +
            "WHERE q.lifeCycleStatus = :lifeCycleStatus " +
             " AND q.id IN (:ids) ")
    List<Question> getQuestionsByIds(List<Long> ids, LifeCycleStatus lifeCycleStatus);

    @Query(value = "SELECT q FROM Question q " +
            "INNER JOIN q.program qc " +
            "WHERE  (:code IS NULL OR q.tableCode LIKE CONCAT('%', :code, '%')) " +
            "AND (:question IS NULL OR q.question LIKE CONCAT('%', :question, '%')) " +
            "AND (:programId IS NULL OR qc.id = :programId)",
            countQuery = "SELECT COUNT(q) FROM Question q " +
                    "INNER JOIN q.program qc " +
                    "WHERE (:code IS NULL OR q.tableCode LIKE CONCAT('%', :code, '%')) " +
                    "AND (:question IS NULL OR q.question LIKE CONCAT('%', :question, '%')) " +
                    "AND (:programId IS NULL OR qc.id = :programId)")
    Page<Question> search(@Nullable String code, @Nullable String question, @Nullable Long programId, Pageable pageable);



    Optional<Question> findByUuid(String uuid);

    @Query(value = "select * from questions where LIFE_CYCLE_STATUS = 'ACTIVE' ", nativeQuery = true)
    List<Question> getByPageAndSize(Pageable pageable);

    @Query("SELECT q FROM Question q WHERE q.program = :program ORDER BY q.createdAt DESC")
    Optional<Question> findTopByProgramOrderByCreatedAtDesc(Program program);

    @Query("SELECT CASE WHEN COUNT(fsq) > 0 THEN true ELSE false END " +
            "FROM FormSectionQuestion fsq " +
            "WHERE fsq.question = :question")
    boolean existsInFormSectionQuestion(Question question);

}
