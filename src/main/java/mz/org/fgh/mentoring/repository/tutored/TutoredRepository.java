package mz.org.fgh.mentoring.repository.tutored;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;

import java.util.List;
import java.util.Optional;


public interface TutoredRepository extends JpaRepository<Tutored, Long> {

    List<Tutored> findAll();

    @Query(value = "select * from tutoreds limit :limi offset :off", nativeQuery = true)
    List<Tutored> findTutoredWithLimit (long limi, long off);

    @Query("From Tutored td inner join fetch td.career c inner join fetch c.tutors t where t.uuid = :tutorUuid")
    List<Tutored> findTutoredByTutorUuid(String tutorUuid);

    @Query("From Tutored t inner join fetch t.employee e  where t.uuid = :uuid")
    List<Tutored> findTutoredByUuid(String uuid);

    List<Tutored> search( Long nuit, String name,User user, String phoneNumber);

    @Query("SELECT DISTINCT t FROM Tutored t " +
            "JOIN t.employee e " +
            "JOIN e.locations l " +
            "JOIN l.district d " +
            "JOIN l.healthFacility hf " +
            "LEFT JOIN FETCH t.menteeFlowHistories mfh " +
            "LEFT JOIN FETCH mfh.flowHistory fh " +
            "LEFT JOIN FETCH mfh.ronda r " +
            "WHERE l.lifeCycleStatus = 'ACTIVE' " +
            "AND hf.lifeCycleStatus = 'ACTIVE' " +
            "AND hf.uuid IN (:uuids)")
    List<Tutored> getTutoredsByHealthFacilityUuids(
            List<String> uuids,
            Pageable pageable
    );

    @Query(
            value = "SELECT DISTINCT t FROM Tutored t " +
                    "JOIN t.employee e " +
                    "JOIN e.locations l " +
                    "JOIN l.district d " +
                    "JOIN l.healthFacility hf " +
                    "LEFT JOIN FETCH t.menteeFlowHistories mfh " +
                    "LEFT JOIN FETCH mfh.flowHistory fh " +
                    "LEFT JOIN FETCH mfh.ronda r " +
                    "WHERE l.lifeCycleStatus = 'ACTIVE' " +
                    "AND hf.lifeCycleStatus = 'ACTIVE' " +
                    "AND hf.uuid IN (:healFacilitiesUuids)",
            countQuery = "SELECT COUNT(DISTINCT t.id) FROM Tutored t " +
                    "JOIN t.employee e " +
                    "JOIN e.locations l " +
                    "JOIN l.district d " +
                    "JOIN l.healthFacility hf " +
                    "WHERE l.lifeCycleStatus = 'ACTIVE' " +
                    "AND hf.lifeCycleStatus = 'ACTIVE' " +
                    "AND hf.uuid IN (:healFacilitiesUuids)"
    )
    Page<Tutored> findAllOfHealthFacilities(List<String> healFacilitiesUuids, Pageable pageable);

    @Query("SELECT t FROM Tutored t LEFT JOIN FETCH t.menteeFlowHistories WHERE t.uuid = :uuid")
    Optional<Tutored> findByUuid(String uuid);

    @Query("SELECT COUNT(mfh) FROM MenteeFlowHistory mfh WHERE mfh.tutored.id = :tutoredId")
    long countMenteeFlowHistoriesByTutoredId(Long tutoredId);

}
