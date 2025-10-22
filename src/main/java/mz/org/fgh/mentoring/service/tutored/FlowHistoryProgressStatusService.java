package mz.org.fgh.mentoring.service.tutored;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.base.BaseService;
import mz.org.fgh.mentoring.entity.tutored.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus;
import mz.org.fgh.mentoring.repository.tutored.FlowHistoryProgressStatusRepository;

import javax.transaction.Transactional;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class FlowHistoryProgressStatusService extends BaseService {

    private final FlowHistoryProgressStatusRepository flowHistoryProgressStatusRepository;

    public FlowHistoryProgressStatusService(FlowHistoryProgressStatusRepository flowHistoryProgressStatusRepository) {
        this.flowHistoryProgressStatusRepository = flowHistoryProgressStatusRepository;
    }

    public Optional<FlowHistoryProgressStatus> findById(Long id) {
        return flowHistoryProgressStatusRepository.findById(id);
    }

    public Optional<FlowHistoryProgressStatus> findByName(String name) {
        return flowHistoryProgressStatusRepository.findByName(name);
    }

    @Transactional
    public FlowHistoryProgressStatus save(FlowHistoryProgressStatus progressStatus) {
        return flowHistoryProgressStatusRepository.save(progressStatus);
    }

    public Map<EnumFlowHistoryProgressStatus, FlowHistoryProgressStatus> findAllByNames(Set<EnumFlowHistoryProgressStatus> statuses) {
        return statuses.stream()
                .collect(Collectors.toMap(
                        status -> status,
                        status -> findByName(status.name())
                                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND,
                                        "Estado não encontrado: " + status.name()))
                ));
    }

}
