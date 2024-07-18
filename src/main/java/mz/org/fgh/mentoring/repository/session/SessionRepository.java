package mz.org.fgh.mentoring.repository.session;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.session.Session;

import java.util.Date;
import java.util.List;
import java.util.Optional;


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
            "WHERE s.startDate =:startDate " +
            "AND  s.endDate is null ")
    List<Session> getAllOfRondaPending(Date startDate);

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
    List<Session> findAllOfRonda(Long rondaId);
}
