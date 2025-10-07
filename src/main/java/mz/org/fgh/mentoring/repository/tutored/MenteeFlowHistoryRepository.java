package mz.org.fgh.mentoring.repository.tutored;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.enums.FlowHistoryProgressStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenteeFlowHistoryRepository extends JpaRepository<MenteeFlowHistory, Long> {

    @Override
    List<MenteeFlowHistory> findAll();

    @Override
    Optional<MenteeFlowHistory> findById(Long id);

    Optional<MenteeFlowHistory> findTopByTutoredOrderByCreatedAtDesc(Tutored tutored);

    @Query("select fh from MenteeFlowHistory mfh " +
            "join mfh.flowHistory fh " +
            "where fh.name = :code")
    Optional<MenteeFlowHistory> findFlowHistoryByCode(String code);

    @Query("select mfh from MenteeFlowHistory mfh where mfh.tutored.uuid = :tutoredUuid")
    List<MenteeFlowHistory> findByTutoredUuid(String tutoredUuid);

    @Query("select mfh from MenteeFlowHistory mfh " +
            "join fetch mfh.tutored t " +
            "where mfh.flowHistory.name = :flowHistoryName " +
            "and mfh.progressStatus = :progressStatus")
    List<MenteeFlowHistory> findByFlowHistoryNameAndProgressStatus(String flowHistoryName, FlowHistoryProgressStatus progressStatus);

}
