package mz.org.fgh.mentoring.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;

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
    private String finalScore;
    private RondaDTO ronda;
    Map<String, List<String>> summaryDetailsMap;
    @JsonIgnore
    Map<Integer, List<SessionSummary>> summaryDetails;
}
