package mz.org.fgh.mentoring.entity.tutored;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.tutored.FlowHistoryDTO;

import javax.persistence.*;

@Schema(name = "FlowHistory", description = "Defines the stages or events of a mentee's mentoring flow")
@Entity
@Table(name = "flow_histories")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class FlowHistory extends BaseEntity {

    @Column(name = "NAME", nullable = false)
    private String name; // Ex.: NOVO, SESSAO_ZERO, RONDA_MENTORIA, SESSAO_SEMESTRAL, etc.

    @Column(name = "DESCRIPTION", length = 500)
    private String description; // Texto legível/descritivo da ronda/sessão

    @Creator
    public FlowHistory() {}

    public FlowHistory(String uuid) {
        super(uuid);
    }

    @Override
    public String toString() {
        return "FlowHistory{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
