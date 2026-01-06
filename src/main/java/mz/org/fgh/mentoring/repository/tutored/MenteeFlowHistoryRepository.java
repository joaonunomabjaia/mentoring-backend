package mz.org.fgh.mentoring.repository.tutored;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenteeFlowHistoryRepository extends JpaRepository<MenteeFlowHistory, Long> {

    @Override
    List<MenteeFlowHistory> findAll();

    @Override
    Optional<MenteeFlowHistory> findById(Long id);

    Optional<MenteeFlowHistory> findTopByTutoredOrderByCreatedAtDesc(Tutored tutored);

    @Query("""
        SELECT fh
        FROM MenteeFlowHistory mfh
        JOIN mfh.flowHistory fh
        WHERE fh.name = :code
    """)
    Optional<MenteeFlowHistory> findFlowHistoryByCode(String code);

    @Query("SELECT mfh FROM MenteeFlowHistory mfh WHERE mfh.tutored.uuid = :tutoredUuid")
    List<MenteeFlowHistory> findByTutoredUuid(String tutoredUuid);

    @Query(
            value = """
        SELECT mfh.*
        FROM mentee_flow_histories mfh
        JOIN tutoreds t ON mfh.TUTORED_ID = t.id
        JOIN employee e ON t.EMPLOYEE_ID = e.id
        JOIN flow_histories fh ON mfh.FLOW_HISTORY_ID = fh.id
        LEFT JOIN rondas r ON mfh.RONDA_ID = r.id
        WHERE (
            :menteeName IS NULL
            OR SOUNDEX(e.name) = SOUNDEX(:menteeName)
            OR SOUNDEX(e.surname) = SOUNDEX(:menteeName)
            OR LOWER(e.name) LIKE LOWER(CONCAT('%', :menteeName, '%'))
            OR LOWER(e.surname) LIKE LOWER(CONCAT('%', :menteeName, '%'))
        )
          AND (:progressStatus IS NULL OR mfh.PROGRESS_STATUS = :progressStatus)
          AND (:flowHistoryName IS NULL OR fh.NAME = :flowHistoryName)
          AND (:startDate IS NULL OR mfh.CREATED_AT >= :startDate)
          AND (:endDate IS NULL OR mfh.CREATED_AT <= :endDate)
        ORDER BY mfh.CREATED_AT DESC
    """,
            countQuery = """
        SELECT COUNT(*)
        FROM mentee_flow_histories mfh
        JOIN tutoreds t ON mfh.TUTORED_ID = t.id
        JOIN employee e ON t.EMPLOYEE_ID = e.id
        JOIN flow_histories fh ON mfh.FLOW_HISTORY_ID = fh.id
        LEFT JOIN rondas r ON mfh.RONDA_ID = r.id
        WHERE (
            :menteeName IS NULL
            OR SOUNDEX(e.name) = SOUNDEX(:menteeName)
            OR SOUNDEX(e.surname) = SOUNDEX(:menteeName)
            OR LOWER(e.name) LIKE LOWER(CONCAT('%', :menteeName, '%'))
            OR LOWER(e.surname) LIKE LOWER(CONCAT('%', :menteeName, '%'))
        )
          AND (:progressStatus IS NULL OR mfh.PROGRESS_STATUS = :progressStatus)
          AND (:flowHistoryName IS NULL OR fh.NAME = :flowHistoryName)
          AND (:startDate IS NULL OR mfh.CREATED_AT >= :startDate)
          AND (:endDate IS NULL OR mfh.CREATED_AT <= :endDate)
    """,
            nativeQuery = true
    )
    Page<MenteeFlowHistory> findFiltered(
            @Nullable String menteeName,
            @Nullable String progressStatus,
            @Nullable String flowHistoryName,
            @Nullable Date startDate,
            @Nullable Date endDate,
            Pageable pageable
    );


    Optional<MenteeFlowHistory> findByUuid(String uuid);

    int countByTutored(Tutored tutored);

    /**
     * Filtra MenteeFlowHistory pelo flowHistory.code e progressStatus.code desejados (RONDA/CICLO -> INICIO).
     * Traz apenas aqueles cujo tutored não tem nenhuma Mentorship realizada nos últimos 60 dias.
     * Pegar ultimo de cada mentorando
     */
    @Query("""
    SELECT mfh
    FROM MenteeFlowHistory mfh
    JOIN mfh.flowHistory fh
    JOIN mfh.progressStatus ps
    JOIN mfh.tutored t
    WHERE fh.code = :flowHistoryCode
      AND ps.code = :flowHistoryStatusCode
      AND mfh.sequenceNumber = (
          SELECT MAX(mfh2.sequenceNumber)
          FROM MenteeFlowHistory mfh2
          WHERE mfh2.tutored = t
      )
      AND NOT EXISTS (
          SELECT 1 FROM Mentorship m
          WHERE m.tutored = t
            AND m.performedDate > CURRENT_DATE - :menteeRondaRemovalInterval
      )
""")
    List<MenteeFlowHistory> findRondasOuCicloAtcIniciadasSemMentorshipHaMaisDe60Dias(
            String flowHistoryCode,
            String flowHistoryStatusCode,
            int menteeRondaRemovalInterval
    );





    /**
     * Retorna todos os MenteeFlowHistory com FlowHistory "RONDA / CICLO ATC"
     * e ProgressStatus "TERMINADO", criados há 6 meses ou mais.
     */
    @Query("""
        SELECT mfh
        FROM MenteeFlowHistory mfh
        JOIN mfh.flowHistory fh
        JOIN mfh.progressStatus ps
        WHERE fh.name = 'RONDA / CICLO ATC'
          AND ps.name = 'TERMINADO'
          AND mfh.createdAt <= CURRENT_DATE - 180
    """)
    List<MenteeFlowHistory> findRondaTerminadaHaMaisDe6Meses();

    void deleteByTutored(Tutored tutored);

    @Query("DELETE FROM MenteeFlowHistory m WHERE m.tutored.id = :tutoredId")
    long deleteByTutoredId(Long tutoredId);

    void deleteByRonda(Ronda ronda);

    List<MenteeFlowHistory> findByTutoredOrderBySequenceNumberAsc(Tutored tutored);

    List<MenteeFlowHistory> findByFlowHistoryCodeAndProgressStatusCode(String flowCode, String statusCode);

    Optional<MenteeFlowHistory> findTopByTutoredOrderBySequenceNumberDesc(Tutored tutored);

    @Query("SELECT mfh FROM MenteeFlowHistory mfh " +
            "JOIN FETCH mfh.tutored t " +
            "JOIN FETCH mfh.ronda r " +
            "JOIN FETCH mfh.flowHistory fh " +
            "JOIN FETCH mfh.progressStatus ps " +
            "WHERE fh.code = :flowCode " +
            "AND ps.code = :statusCode " +
            "AND mfh.lifeCycleStatus = :lifeCycleStatus")
    List<MenteeFlowHistory> findActiveByFlowAndStatus(String flowCode,
                                                      String statusCode,
                                                      LifeCycleStatus lifeCycleStatus);

    List<MenteeFlowHistory> findAllByFlowHistoryCodeAndProgressStatusCode(String flowCode, String statusCode);

    List<MenteeFlowHistory> findByRonda(Ronda ronda);

    List<MenteeFlowHistory> findByTutored(Tutored tutored);

    @Query("UPDATE MenteeFlowHistory SET lifeCycleStatus = :lifeCycleStatus WHERE tutored.id = :tutoredId AND lifeCycleStatus = 'ACTIVE'")
    void inactivatePreviousHistories(Long tutoredId, LifeCycleStatus lifeCycleStatus);

    @Query("""
           SELECT m
           FROM MenteeFlowHistory m
           WHERE m.tutored.uuid = :uuid
             AND (m.sequenceNumber = 1 OR m.lifeCycleStatus = :active)
        """)
    List<MenteeFlowHistory> findInitialAndActiveByTutoredUuid(String uuid, LifeCycleStatus active);

}
