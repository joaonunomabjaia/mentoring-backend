package mz.org.fgh.mentoring.service.tutored;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.base.BaseService;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;
import mz.org.fgh.mentoring.repository.tutored.FlowHistoryRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Singleton
public class FlowHistoryService extends BaseService {

    private final FlowHistoryRepository flowHistoryRepository;

    public FlowHistoryService(FlowHistoryRepository flowHistoryRepository) {
        this.flowHistoryRepository = flowHistoryRepository;
    }

    public Optional<FlowHistory> findById(Long id) {
        return flowHistoryRepository.findById(id);
    }

    public Optional<FlowHistory> findByName(String name) {
        return flowHistoryRepository.findByName(name);
    }

    @Transactional
    public FlowHistory save(FlowHistory flowHistory) {
        return flowHistoryRepository.save(flowHistory);
    }
}
