package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutor.TutorInternalLocation;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface TutorInternalLocationRepository
        extends JpaRepository<TutorInternalLocation, Long> {

    Optional<TutorInternalLocation> findByTutorAndLifeCycleStatus(
            Tutor tutor,
            LifeCycleStatus lifeCycleStatus
    );

    List<TutorInternalLocation> findAllByTutorOrderByCreatedAtDesc(Tutor tutor);

    List<TutorInternalLocation> findAllByTutorAndLifeCycleStatus(
            Tutor tutor,
            LifeCycleStatus lifeCycleStatus
    );

    @Query("""
       SELECT il
       FROM TutorInternalLocation il
       JOIN FETCH il.location hf
       WHERE il.tutor.uuid = :tutorUuid
         AND il.lifeCycleStatus = 'ACTIVE'
    """)
        Optional<TutorInternalLocation> findActiveByTutorUuidWithHealthFacility(String tutorUuid);

}
