package mz.org.fgh.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.entity.tutored.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenteeFlowHistoryDTO extends BaseEntityDTO {

    private TutoredDTO tutoredDTO;
    private FlowHistoryDTO flowHistoryDTO;
    private FlowHistoryProgressStatus progressStatus;
    private RondaDTO rondaDTO;
    private double classification;

    public MenteeFlowHistoryDTO(MenteeFlowHistory entity) {
        super(entity);
        this.progressStatus = entity.getProgressStatus();
        this.classification = entity.getClassification();

        if (entity.getTutored() != null) {
            this.tutoredDTO = new TutoredDTO(entity.getTutored());
        }
        if (entity.getFlowHistory() != null) {
            this.flowHistoryDTO = new FlowHistoryDTO(entity.getFlowHistory());
        }
        if (entity.getRonda() != null) {
            this.rondaDTO = new RondaDTO(entity.getRonda());
        }
    }

    @JsonIgnore
    public MenteeFlowHistory toEntity() {
        MenteeFlowHistory entity = new MenteeFlowHistory();
        entity.setId(this.getId());
        entity.setUuid(this.getUuid());
        entity.setCreatedAt(this.getCreatedAt());
        entity.setUpdatedAt(this.getUpdatedAt());
        entity.setProgressStatus(this.getProgressStatus());
        entity.setClassification(this.getClassification());

        if (Utilities.stringHasValue(this.getLifeCycleStatus())) {
            entity.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        }
        if (this.tutoredDTO != null) {
            entity.setTutored(this.tutoredDTO.toEntity());
        }
        if (this.flowHistoryDTO != null) {
            entity.setFlowHistory(this.flowHistoryDTO.toEntity());
        }
//        if (this.rondaDTO != null) {
//            entity.setRonda(this.rondaDTO.toEntity());
//        }

        return entity;
    }
}

