package mz.org.fgh.mentoring.enums;

import lombok.Getter;

@Getter
public enum EnumFlowHistory {

    NOVO("NOVO"),
    SESSAO_ZERO("SESSÃO ZERO"),
    RONDA_CICLO("RONDA / CICLO ATC"),
    SESSAO_SEMESTRAL("SESSÃO SEMESTRAL");

    private final String label;

    EnumFlowHistory(String label) {
        this.label = label;
    }

}
