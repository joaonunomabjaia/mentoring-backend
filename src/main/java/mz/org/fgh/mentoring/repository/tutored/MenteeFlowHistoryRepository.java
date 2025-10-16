package mz.org.fgh.mentoring.repository.tutored;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;

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
     * Retorna todos os MenteeFlowHistory com FlowHistory "RONDA / CICLO ATC"
     * e ProgressStatus "INICIO", criados há 60 ou mais dias.
     */
    @Query("""
        SELECT mfh
        FROM MenteeFlowHistory mfh
        JOIN mfh.flowHistory fh
        JOIN mfh.progressStatus ps
        WHERE (fh.name = 'RONDA / CICLO ATC')
          AND ps.name = 'INICIO'
          AND mfh.createdAt <= CURRENT_DATE - 60
    """)
    List<MenteeFlowHistory> findRondasOuCicloAtcIniciadasHaMaisDe60Dias();


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

}
