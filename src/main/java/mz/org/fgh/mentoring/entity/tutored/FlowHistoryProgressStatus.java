package mz.org.fgh.mentoring.entity.tutored;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.tutored.FlowHistoryProgressStatusDTO;
import mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus;

import javax.persistence.*;

@Schema(name = "FlowHistoryProgressStatus",
        description = "Progress status of a flow history (e.g. INICIO, ISENTO, AGUARDA_INICIO, TERMINADO, INTERROMPIDO)")
@Entity
@Table(
        name = "flow_history_progress_statuses",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_FHPS_CODE", columnNames = "CODE")
        }
)
@Data
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Getter
@Setter
public class FlowHistoryProgressStatus extends BaseEntity {

    /** Canonical code (enum name) */
    @EqualsAndHashCode.Include
    @Column(name = "CODE", nullable = false, unique = true, length = 64)
    private String code;

    /** Human-readable label (kept for UI/legacy data) */
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "DESCRIPTION", length = 250)
    private String description;

    @Creator public FlowHistoryProgressStatus() {}
    public FlowHistoryProgressStatus(String uuid) { super(uuid); }

    /** Factory from enum */
    public static FlowHistoryProgressStatus of(EnumFlowHistoryProgressStatus e) {
        return new FlowHistoryProgressStatus()
                .setCode(e.getCode())       // e.name()
                .setName(e.getLabel())
                .setDescription(null);
    }

    /** Backward-safe DTO ctor (accepts code or label) */
    public FlowHistoryProgressStatus(FlowHistoryProgressStatusDTO dto) {
        super(dto);
        String dtoCode = dto.getCode();       // add getCode() to DTO if not present yet
        String dtoName = dto.getName();

        if (dtoCode != null) {
            this.code = dtoCode;
            this.name = (dtoName != null) ? dtoName : dtoCode;
        } else {
            // legacy path: name might be a label; normalize via enum when possible
            try {
                EnumFlowHistoryProgressStatus e = EnumFlowHistoryProgressStatus.fromCode(dtoName);
                this.code = e.getCode();
                this.name = e.getLabel();
            } catch (Exception ignore) {
                this.code = sanitizeToCode(dtoName);
                this.name = dtoName;
            }
        }
        this.description = dto.getDescription();
    }

    private static String sanitizeToCode(String s) {
        if (s == null) return "UNKNOWN";
        String noAccent = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return noAccent.toUpperCase().replaceAll("[^A-Z0-9]+", "_");
    }

    @Override
    public String toString() {
        return "FlowHistoryProgressStatus{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
