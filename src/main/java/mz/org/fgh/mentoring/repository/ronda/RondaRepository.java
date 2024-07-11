package mz.org.fgh.mentoring.repository.ronda;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.core.annotation.Nullable;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaMentee;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RondaRepository extends CrudRepository<Ronda, Long> {

    @Override
    List<Ronda> findAll();

    @Override
    Optional<Ronda> findById(@NotNull Long id);

    Optional<Ronda> findByUuid(String uuid);

    @Query(value = "select * from rondas limit :limi offset :off", nativeQuery = true)
    List<Ronda> findRondaWithLimit(long limi, long off);

    @Query("select DISTINCT(r) from RondaMentor rm " +
            "INNER JOIN FETCH rm.ronda r " +
            "INNER JOIN FETCH pa.rondaType rt " +
            "where rm.id =: mentorId AND rm.lifeCycleStatus = :lifeCycleStatus AND r.lifeCycleStatus = :lifeCycleStatus ")
    List<Ronda> getAllRondasOfMentor(Long mentorId, LifeCycleStatus lifeCycleStatus);

    List<Ronda> getAllOfMentor(String mentorUuid);

//    List<Ronda> changeMentor(Long rondaId, Long newMentorId);

    @Query("select r from Ronda r where r.createdBy = :userUuid")
    List<Ronda> getByUserUuid(String userUuid);

    @Query("select r from Ronda r join fetch r.healthFacility hf where hf.id = :healthFacilityId")
    List<Ronda> getByHealthFacilityId(Long healthFacilityId);


    @Query("select r from Ronda r " +
            "INNER JOIN FETCH r.rondaMentees rme " +
            "INNER JOIN FETCH rme.tutored t " +
            "INNER JOIN FETCH r.rondaMentors rmr " +
            "INNER JOIN FETCH rmr.mentor m")
            List<Ronda> findAllRondaWithAll();



    @Query("select distinct r from Ronda r " +
            "INNER JOIN FETCH r.healthFacility hf " +
            "INNER JOIN FETCH hf.district d " +
            "INNER JOIN FETCH d.province p " +
            "INNER JOIN FETCH r.rondaMentors rm " +
            "INNER JOIN FETCH rm.mentor m " +
            "where (:provinceId is null or p.id = :provinceId) " +
            "and (:districtId is null or d.id = :districtId) " +
            "and (:healthFacilityId is null or hf.id = :healthFacilityId) " +
            "and (:mentorId is null or m.id = :mentorId) " +
            "and (:startDate is null or r.startDate >= :startDate) " +
            "and (:endDate is null or r.endDate <= :endDate)")
    List<Ronda> search(@Nullable Long provinceId, @Nullable Long districtId, @Nullable Long healthFacilityId, @Nullable Long mentorId, @Nullable Date startDate, @Nullable Date endDate);

//    @Query(value = "SELECT r FROM Ronda r " +
//            "INNER JOIN FETCH r.healthFacility hf " +
//            "INNER JOIN FETCH hf.district d " +
//            "INNER JOIN FETCH d.province p " +
//            "INNER JOIN FETCH r.rondaMentors rm " +
//            "INNER JOIN FETCH rm.mentor m " +
//            "WHERE (:provinceId IS NULL OR p.id = :provinceId) " +
//            "AND (:districtId IS NULL OR d.id = :districtId) " +
//            "AND (:healthFacilityId IS NULL OR hf.id = :healthFacilityId) " +
//            "AND (:mentorId IS NULL OR m.id = :mentorId) " +
//            "AND (:startDate IS NULL OR r.startDate >= :startDate) " +
//            "AND (:endDate IS NULL OR r.endDate <= :endDate)")
//    List<Ronda> search(
//            Long provinceId,
//            @Nullable Long districtId,
//            @Nullable Long healthFacilityId,
//            @Nullable Long mentorId,
//            @Nullable Date startDate,
//            @Nullable Date endDate
//    );

    @Query("select r from Ronda r where r.endDate IS NULL and r.uuid IN (:rondasUuids)")
    List<Ronda> findRondasByUuids(List<String> rondasUuids);

}
