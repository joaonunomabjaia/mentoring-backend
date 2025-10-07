package mz.org.fgh.mentoring.service.tutored;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.base.BaseService;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.enums.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.enums.FlowHistoryStatus;
import mz.org.fgh.mentoring.repository.tutored.MenteeFlowHistoryRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Singleton
public class MenteeFlowHistoryService extends BaseService {

    private final MenteeFlowHistoryRepository menteeFlowHistoryRepository;

    public MenteeFlowHistoryService(MenteeFlowHistoryRepository menteeFlowHistoryRepository) {
        this.menteeFlowHistoryRepository = menteeFlowHistoryRepository;
    }

    public Optional<MenteeFlowHistory> findById(Long id) {
        return menteeFlowHistoryRepository.findById(id);
    }

    @Transactional
    public MenteeFlowHistory save(MenteeFlowHistory history) {
        return menteeFlowHistoryRepository.save(history);
    }

    public MenteeFlowHistory findLastByTutored(Tutored tutored) {
        return menteeFlowHistoryRepository.findTopByTutoredOrderByCreatedAtDesc(tutored).orElse(null);
    }

//    public FlowHistory findFlowHistoryByCode(String code) {
//        return menteeFlowHistoryRepository.findFlowHistoryByCode(code)
//                .orElseThrow(() -> new RuntimeException("FlowHistory não encontrado: " + code));
//    }

    public List<MenteeFlowHistory> findCompletedRondaMentoria() {
        return menteeFlowHistoryRepository.findByFlowHistoryNameAndProgressStatus(
                FlowHistoryStatus.RONDA_MENTORIA.name(),
                FlowHistoryProgressStatus.FEITO
        );
    }

}
