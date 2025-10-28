package mz.org.fgh.mentoring.dto.tutored;

import lombok.Builder;

@Builder
public record FlowHistoryMenteeAuxDTO(
        String estagio,
        String estado,
        double classificacao
) {}