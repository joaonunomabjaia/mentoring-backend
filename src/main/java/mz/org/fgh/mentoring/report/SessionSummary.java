package mz.org.fgh.mentoring.report;

import lombok.Data;

@Data
public class SessionSummary {
    private String title;
    private int simCount;
    private int naoCount;
    private double progressPercentage;

    // Constructor
    public SessionSummary(String title, int simCount, int naoCount, double progressPercentage) {
        this.title = title;
        this.simCount = simCount;
        this.naoCount = naoCount;
        this.progressPercentage = progressPercentage;
    }

    public SessionSummary() {
    }
}
