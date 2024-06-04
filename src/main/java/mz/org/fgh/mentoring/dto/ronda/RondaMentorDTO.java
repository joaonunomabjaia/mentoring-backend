package mz.org.fgh.mentoring.dto.ronda;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.entity.ronda.RondaMentor;

@Data
@NoArgsConstructor
public class RondaMentorDTO extends BaseEntityDTO {
    private Date startDate;

    private Date endDate;

    private TutorDTO mentor;

    private RondaDTO ronda;

    public RondaMentorDTO(RondaMentor rondaMentor) {
        super(rondaMentor);
        this.setMentor(new TutorDTO(rondaMentor.getMentor()));
        this.setRonda(new RondaDTO(rondaMentor.getRonda()));
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public TutorDTO getMentor() {
        return mentor;
    }

    public void setMentor(TutorDTO mentor) {
        this.mentor = mentor;
    }

    public RondaDTO getRonda() {
        return ronda;
    }

    public void setRonda(RondaDTO ronda) {
        this.ronda = ronda;
    }
    public RondaMentor getRondaMentor() {
        RondaMentor rondaMentor = new RondaMentor();
        rondaMentor.setUuid(this.getUuid());
        rondaMentor.setStartDate(this.getStartDate());
        rondaMentor.setEndDate(this.getEndDate());
        rondaMentor.setCreatedAt(this.getCreatedAt());
        rondaMentor.setUpdatedAt(this.getUpdatedAt());
        if(this.getMentor()!=null) {
            rondaMentor.setMentor(this.getMentor().getTutor());
        }
        if(this.getRonda()!=null) {
            rondaMentor.setRonda(this.getRonda().getRonda());
        }
        return rondaMentor;
    }
}
