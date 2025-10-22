package mz.org.fgh.mentoring.repository.tutored;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.tutored.FlowHistoryProgressStatus;

import java.util.Optional;

@Repository
public interface FlowHistoryProgressStatusRepository extends JpaRepository<FlowHistoryProgressStatus, Long> {
    Optional<FlowHistoryProgressStatus> findByName(String name);
}
