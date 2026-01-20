package mz.org.fgh.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Carries ENUM CODES (not labels) for stage and status.
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "FlowHistoryMenteeAuxDTO")
public record FlowHistoryMenteeAuxDTO(
        @Schema(description = "Enum code of the flow stage", example = "RONDA_CICLO")
        String estagio,

        @Schema(description = "Enum code of the progress status", example = "AGUARDA_INICIO")
        String estado,

        @Schema(description = "Optional classification (0–100). Null when N/A")
        Double classificacao,

        String rondaUUID,

        Integer seq
) {}
