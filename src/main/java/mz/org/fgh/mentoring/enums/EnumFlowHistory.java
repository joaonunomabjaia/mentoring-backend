package mz.org.fgh.mentoring.enums;

import lombok.Getter;

@Getter
public enum EnumFlowHistory {

    NOVO("NOVO", "Novo registo"),
    SESSAO_ZERO("SESSAO_ZERO", "Sessão zero"),
    RONDA_CICLO("RONDA_CICLO", "Ronda / Ciclo ATC"),
    SESSAO_SEMESTRAL("SESSAO_SEMESTRAL", "Sessão semestral");

    private final String code;
    private final String label;

    EnumFlowHistory(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static EnumFlowHistory fromCode(String code) {
        if (code == null) return null;
        String normalized = code.trim().toUpperCase();
        for (EnumFlowHistory e : values()) {
            if (e.code.equalsIgnoreCase(normalized) || e.name().equalsIgnoreCase(normalized)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown EnumFlowHistory code: " + code);
    }
}
