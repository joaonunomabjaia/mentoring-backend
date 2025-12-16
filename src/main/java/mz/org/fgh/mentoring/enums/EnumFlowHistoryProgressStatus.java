package mz.org.fgh.mentoring.enums;

import lombok.Getter;

@Getter
public enum EnumFlowHistoryProgressStatus {

    INICIO("INICIO", "Início"),
    ISENTO("ISENTO", "Isento"),
    AGUARDA_INICIO("AGUARDA_INICIO", "Aguarda início"),
    TERMINADO("TERMINADO", "Terminado"),
    INTERROMPIDO("INTERROMPIDO", "Interrompido");

    private final String code;
    private final String label;

    EnumFlowHistoryProgressStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static EnumFlowHistoryProgressStatus fromCode(String code) {
        if (code == null) return null;
        String normalized = code.trim().toUpperCase();
        for (EnumFlowHistoryProgressStatus s : values()) {
            if (s.code.equalsIgnoreCase(normalized) || s.name().equalsIgnoreCase(normalized)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown EnumFlowHistoryProgressStatus code: " + code);
    }
}
