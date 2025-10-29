package mz.org.fgh.mentoring.entity.tutored;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.tutored.FlowHistoryDTO;
import mz.org.fgh.mentoring.enums.EnumFlowHistory;

import javax.persistence.*;

@Schema(name = "FlowHistory", description = "Stages of a mentee's mentoring flow")
@Entity
@Table(
        name = "flow_histories",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_FLOW_HISTORIES_CODE", columnNames = "CODE")
        }
)
@Data
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class FlowHistory extends BaseEntity {

    /** Canonical code (enum name) */
    @EqualsAndHashCode.Include
    @Column(name = "CODE", nullable = false, unique = true, length = 64)
    private String code;

    /** Human-readable label kept for UI/legacy */
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Creator public FlowHistory() {}
    public FlowHistory(String uuid) { super(uuid); }

    /** Factory from enum */
    public static FlowHistory of(EnumFlowHistory e) {
        return new FlowHistory()
                .setCode(e.getCode())    // e.name()
                .setName(e.getLabel())
                .setDescription(null);
    }

    /** Backward-safe DTO ctor (accepts code or label) */
    public FlowHistory(FlowHistoryDTO dto) {
        super(dto);
        String dtoCode = dto.getCode();   // add getCode() to DTO if not present yet
        String dtoName = dto.getName();

        if (dtoCode != null) {
            this.code = dtoCode;
            this.name = (dtoName != null) ? dtoName : dtoCode;
        } else {
            // legacy: infer code from label when possible
            try {
                EnumFlowHistory e = EnumFlowHistory.fromCode(dtoName);
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
        return "FlowHistory{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
