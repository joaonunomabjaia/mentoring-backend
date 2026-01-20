package mz.org.fgh.mentoring.entity.tutored;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.tutored.FlowHistoryMenteeAuxDTO;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.session.SessionRecommendedResource;
import mz.org.fgh.mentoring.enums.EnumFlowHistory;
import mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Schema(name = "Tutoreds", description = "A professional that provides mentoring to tutored individuals")
@Entity(name = "Tutored")
@Table(name = "tutoreds")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Tutored extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @Column(name = "ZERO_EVALUATION_SCORE")
    private Double zeroEvaluationScore;

    @OneToMany(mappedBy = "tutored")
    private List<SessionRecommendedResource> recommendedResources;


    @Creator
    public Tutored() {}

    public Tutored(String uuid) {
        super(uuid);
    }

    public Tutored(TutoredDTO tutoredDTO) {
        super(tutoredDTO);
        this.setZeroEvaluationScore(tutoredDTO.getZeroEvaluationScore());
        if (tutoredDTO.getEmployeeDTO() != null) this.setEmployee(new Employee(tutoredDTO.getEmployeeDTO()));

        // Converte flowHistoryMenteeAuxDTO -> lista de MenteeFlowHistory
        if (tutoredDTO.getFlowHistoryMenteeAuxDTO() != null
                && !tutoredDTO.getFlowHistoryMenteeAuxDTO().isEmpty()) {

            List<MenteeFlowHistory> histories = new ArrayList<>();

            for (FlowHistoryMenteeAuxDTO aux : tutoredDTO.getFlowHistoryMenteeAuxDTO()) {
                if (aux == null) continue;

                MenteeFlowHistory mfh = new MenteeFlowHistory();
                mfh.setTutored(this); // relação dono

                // FlowHistory por code
                mfh.setFlowHistory(new FlowHistory());
                mfh.getFlowHistory().setCode(aux.estagio());

                // ProgressStatus por code
                mfh.setProgressStatus(new FlowHistoryProgressStatus());
                mfh.getProgressStatus().setCode(aux.estado());

                if (aux.classificacao() != null) mfh.setClassification(aux.classificacao());
                mfh.setSequenceNumber(aux.seq());

                histories.add(mfh);
            }

            this.menteeFlowHistories = histories;
        }
    }

    @Transient
    public boolean isZeroEvaluationDone() {
        return this.zeroEvaluationScore != null && this.zeroEvaluationScore > 0;
    }

    @Override
    public String toString() {
        return "Tutored{" +
                "employee=" + employee +
                '}';
    }

    @OneToMany(mappedBy = "tutored", fetch = FetchType.LAZY)
    private List<MenteeFlowHistory> menteeFlowHistories;

    public Optional<MenteeFlowHistory> getLastMenteeFlowHistory() {
        if (menteeFlowHistories == null || !Hibernate.isInitialized(menteeFlowHistories) || menteeFlowHistories.isEmpty()) {
            return Optional.empty();
        }

        return menteeFlowHistories.stream()
                .filter(m -> m.getSequenceNumber() != null)
                .max(Comparator.comparing(MenteeFlowHistory::getSequenceNumber));
    }

    /**
     * Um tutored pode ser resetado se:
     * 1️⃣ O último MenteeFlowHistory tiver flowHistory.code == "SESSAO_ZERO"
     *     e progressStatus.code == "AGUARDA_INICIO"
     *
     * OU
     *
     * 2️⃣ O total de menteeFlowHistories <= 2
     *     e o último tiver flowHistory.code contendo "RONDA_CICLO"
     *     e progressStatus.code == "AGUARDA_INICIO"
     */
    @Transient
    public boolean canResetMenteeFlowHistory(MenteeFlowHistory newMenteeFlowHistory) {
        Optional<MenteeFlowHistory> lastOpt = getLastMenteeFlowHistory();

        if (lastOpt.isEmpty()) {
            // Aqui lanca uma runtime exception informando que ha uma inconsistencia nos estados
        }

        MenteeFlowHistory last = lastOpt.get();

        if (last.equals(newMenteeFlowHistory)) {
            return false;
        }

        if (last.getFlowHistory() == null || last.getFlowHistory().getCode() == null
                || last.getProgressStatus() == null || last.getProgressStatus().getCode() == null) {
            return true;
        }

        String flowCode = last.getFlowHistory().getCode().trim().toUpperCase();
        String progressCode = last.getProgressStatus().getCode().trim().toUpperCase();

        // Caso 1: Sessão zero aguardando início
        if ("SESSAO_ZERO".equals(flowCode) && "AGUARDA_INICIO".equals(progressCode)) {
            return true;
        }

        // Caso 2: Até 2 registros, e último for RONDA_CICLO aguardando início
        if (menteeFlowHistories != null && menteeFlowHistories.size() <= 2
                && (flowCode.equals(EnumFlowHistory.RONDA_CICLO.getCode()))
                && EnumFlowHistoryProgressStatus.AGUARDA_INICIO.getCode().equals(progressCode)) {
            return true;
        }

        return false;
    }

    public void addFlowHistory(MenteeFlowHistory menteeFlowHistory) {
        if (menteeFlowHistories == null) {
            menteeFlowHistories = new ArrayList<>();
        }
        menteeFlowHistories.add(menteeFlowHistory);
    }

    // ===== Helpers para mapear código -> Enum =====

    private EnumFlowHistory fromFlowCode(String code) {
        if (code == null) return null;
        String normalized = code.trim().toUpperCase();
        for (EnumFlowHistory e : EnumFlowHistory.values()) {
            if (e.getCode() != null && e.getCode().trim().toUpperCase().equals(normalized)) {
                return e;
            }
        }
        return null;
    }

    private EnumFlowHistoryProgressStatus fromStatusCode(String code) {
        if (code == null) return null;
        String normalized = code.trim().toUpperCase();
        for (EnumFlowHistoryProgressStatus s : EnumFlowHistoryProgressStatus.values()) {
            if (s.getCode() != null && s.getCode().trim().toUpperCase().equals(normalized)) {
                return s;
            }
        }
        return null;
    }
}
