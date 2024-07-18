package mz.org.fgh.mentoring.dto.ronda;

import lombok.Data;
import mz.org.fgh.mentoring.report.RondaSummary;

import java.util.List;

@Data
public class RondaReportDTO {

    private List<RondaSummary> rondaSummaryList;
}
