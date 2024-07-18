package mz.org.fgh.mentoring.report;

import lombok.Data;
import mz.org.fgh.mentoring.entity.ronda.Ronda;

import java.util.List;
import java.util.Map;

@Data
public class RondaSummary {
    private long nuit;
    private String mentee;
    private String mentor;
    private double zeroEvaluation;
    private double session1;
    private double session2;
    private double session3;
    private double session4;
    private double finalScore;
    private Ronda ronda;
    Map<Integer, List<SessionSummary>> summaryDetails;
}
