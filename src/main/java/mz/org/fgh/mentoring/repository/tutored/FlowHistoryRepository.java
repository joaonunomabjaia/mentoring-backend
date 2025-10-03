package mz.org.fgh.mentoring.repository.tutored;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;

import java.util.Optional;

@Repository
public interface FlowHistoryRepository extends CrudRepository<FlowHistory, Long> {

    @Override
    Optional<FlowHistory> findById(Long id);

    Optional<FlowHistory> findByName(String name);
}
