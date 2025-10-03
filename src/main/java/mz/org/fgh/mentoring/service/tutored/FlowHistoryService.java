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

    public FlowHistory findByCode(String code) {
        return flowHistoryRepository.findByName(code)
                .orElseThrow(() -> new RuntimeException("FlowHistory não encontrado: " + code));
    }

    @Transactional
    public FlowHistory save(FlowHistory flowHistory) {
        return flowHistoryRepository.save(flowHistory);
    }
}
