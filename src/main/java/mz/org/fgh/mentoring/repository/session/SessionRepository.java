package mz.org.fgh.mentoring.repository.session;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.session.Session;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByUuid(String uuid);

    @Query("SELECT s FROM Session s " +
            "JOIN s.ronda r " +
            "JOIN r.rondaType rt " +
            "JOIN r.rondaMentees rm " +
            "JOIN rm.tutored td " +
            "WHERE rt.code = 'SESSAO_ZERO' " +
            "AND td.id = :tutoredId ")
    Optional<Session> getTutoredZeroSession(Long tutoredId);

    @Query("SELECT s FROM Session s " +
            "INNER JOIN FETCH s.ronda r " +
            "INNER JOIN FETCH s.mentorships m " +
            "INNER JOIN FETCH s.status st " +
            "INNER JOIN FETCH m.tutor t " +
            "INNER JOIN FETCH m.tutored td " +
            "INNER JOIN FETCH m.form f " +
            "INNER JOIN FETCH m.cabinet c " +
            "INNER JOIN FETCH m.evaluationType et " +
            "INNER JOIN FETCH m.door d " +
            "WHERE r.id = :rondaId ")
    Optional<Session> findByRonda(Long rondaId);

    @Query("SELECT s FROM Session s " +
            "INNER JOIN FETCH s.ronda r " +
            "INNER JOIN FETCH s.mentorships m " +
            "INNER JOIN FETCH s.status st " +
            "INNER JOIN FETCH m.tutor t " +
            "INNER JOIN FETCH m.tutored td " +
            "WHERE DATE(s.nextSessionDate) = DATE(:startDate)")
    List<Session> getAllOfRondaPending(Date startDate);

    @Query("SELECT DISTINCT s FROM Session s " +
            "INNER JOIN FETCH s.ronda r " +
            "INNER JOIN FETCH s.status st " +
            "INNER JOIN FETCH s.form f " +
            "INNER JOIN FETCH f.programmaticArea pa " +
            "WHERE r.id = :rondaId")
    Set<Session> findAllOfRonda(Long rondaId);

    @Query("SELECT COUNT(s) FROM Session s " )
    long countAllActiveSessions();

    @Query("SELECT COUNT(s) FROM Session s " +
            "JOIN s.ronda r " +
            "JOIN r.rondaType rt " +
            "WHERE rt.code = :code")
    long countActiveSessionsByRondaTypeCode(String code);

    @Query("SELECT q.question FROM answers a " +
            "JOIN mentorships m ON a.mentorship_id = m.id " +
            "JOIN sessions s ON m.session_id = s.id " +
            "JOIN questions q ON a.question_id = q.id " +
            "WHERE a.value = 'NAO' " +
            "AND m.tutored_id = :tutoredId " +
            "AND s.start_date = (" +
            "   SELECT MAX(s2.start_date) FROM mentorships m2 " +
            "   JOIN sessions s2 ON m2.session_id = s2.id " +
            "   WHERE m2.tutored_id = :tutoredId" +
            ")")
    List<String> findWeakPointsFromLastSessionByTutoredId(Long tutoredId);


    @Query("SELECT s FROM Session s WHERE s.sessionSummary IS NULL ")
    List<Session> findAllWithoutSummary();

    @Query("FROM Session s WHERE s.ronda.id = :rondaId AND s.sessionSummary IS NOT NULL")
    List<Session> findAllWithSummaryByRondaId(Long rondaId);

}
