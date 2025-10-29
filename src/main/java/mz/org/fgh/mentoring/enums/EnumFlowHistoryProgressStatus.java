package mz.org.fgh.mentoring.enums;

public enum EnumFlowHistoryProgressStatus {
    INICIO("INICIO"),
    ISENTO("ISENTO"),
    AGUARDA_INICIO("AGUARDA INICIO"),
    TERMINADO("TERMINADO"),
    INTERROMPIDO("INTERROMPIDO");

    private final String label;
    EnumFlowHistoryProgressStatus(String label) { this.label = label; }

    public String getCode() { return name(); }
    public String getLabel() { return label; }

    public static EnumFlowHistoryProgressStatus fromCode(String code) {
        return EnumFlowHistoryProgressStatus.valueOf(code);
    }
}