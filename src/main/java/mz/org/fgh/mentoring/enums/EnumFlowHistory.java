package mz.org.fgh.mentoring.enums;

public enum EnumFlowHistory {
    NOVO("NOVO"),
    SESSAO_ZERO("SESSÃO ZERO"),
    RONDA_CICLO("RONDA / CICLO ATC"),
    SESSAO_SEMESTRAL("SESSÃO SEMESTRAL");

    private final String label;
    EnumFlowHistory(String label) { this.label = label; }

    public String getCode() { return name(); }     // <- use this everywhere
    public String getLabel() { return label; }

    public static EnumFlowHistory fromCode(String code) {
        return EnumFlowHistory.valueOf(code);
    }
}