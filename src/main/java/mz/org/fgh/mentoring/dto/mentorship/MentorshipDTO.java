package mz.org.fgh.mentoring.dto.mentorship;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.form.FormDTO;
import mz.org.fgh.mentoring.dto.healthFacility.HealthFacilityDTO;
import mz.org.fgh.mentoring.dto.location.CabinetDTO;
import mz.org.fgh.mentoring.dto.session.SessionDTO;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;

@Data
@NoArgsConstructor
public class MentorshipDTO extends BaseEntityDTO {
    private String code;
    private Integer iterationNumber;
    private Date startDate;
    private Date endDate;
    private HealthFacilityDTO healthFacility;
    private TutorDTO mentor;
    private TutoredDTO mentee;
    private SessionDTO session;
    private FormDTO form;
    private CabinetDTO cabinet;
    private DoorDTO door;
    private IterationTypeDTO iterationType;
    private TimeOfDayDTO timeOfDay;
    public MentorshipDTO(Mentorship mentorship) {
        super(mentorship);
        if(mentorship.getHealthFacility()!=null) {
            this.setHealthFacility(new HealthFacilityDTO(mentorship.getHealthFacility()));
        }
        if(mentorship.getTutor()!=null) {
            this.setMentor(new TutorDTO(mentorship.getTutor()));
        }
        if(mentorship.getTutored()!=null) {
            this.setMentee(new TutoredDTO(mentorship.getTutored()));
        }
        if(mentorship.getSession()!=null) {
            this.setSession(new SessionDTO(mentorship.getSession()));
        }
        if(mentorship.getForm()!=null) {
            this.setForm(new FormDTO(mentorship.getForm()));
        }
        if(mentorship.getCabinet()!=null) {
            this.setCabinet(new CabinetDTO(mentorship.getCabinet()));
        }
        if(mentorship.getDoor()!=null) {
            this.setDoor(new DoorDTO(mentorship.getDoor()));
        }
        if(mentorship.getIterationType()!=null) {
            this.setIterationType(new IterationTypeDTO(mentorship.getIterationType()));
        }
        if(mentorship.getTimeOfDay()!=null) {
            this.setTimeOfDay(new TimeOfDayDTO(mentorship.getTimeOfDay()));
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getIterationNumber() {
        return iterationNumber;
    }

    public void setIterationNumber(Integer iterationNumber) {
        this.iterationNumber = iterationNumber;
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

    public HealthFacilityDTO getHealthFacility() {
        return healthFacility;
    }

    public void setHealthFacility(HealthFacilityDTO healthFacility) {
        this.healthFacility = healthFacility;
    }

    public TutorDTO getMentor() {
        return mentor;
    }

    public void setMentor(TutorDTO mentor) {
        this.mentor = mentor;
    }

    public TutoredDTO getMentee() {
        return mentee;
    }

    public void setMentee(TutoredDTO mentee) {
        this.mentee = mentee;
    }

    public SessionDTO getSession() {
        return session;
    }

    public void setSession(SessionDTO session) {
        this.session = session;
    }

    public FormDTO getForm() {
        return form;
    }

    public void setForm(FormDTO form) {
        this.form = form;
    }

    public CabinetDTO getCabinet() {
        return cabinet;
    }

    public void setCabinet(CabinetDTO cabinet) {
        this.cabinet = cabinet;
    }

    public DoorDTO getDoor() {
        return door;
    }

    public void setDoor(DoorDTO door) {
        this.door = door;
    }

    public IterationTypeDTO getIterationType() {
        return iterationType;
    }

    public void setIterationType(IterationTypeDTO iterationType) {
        this.iterationType = iterationType;
    }

    public TimeOfDayDTO getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(TimeOfDayDTO timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public Mentorship getMentorship() {
        Mentorship mentorship = new Mentorship();
        mentorship.setId(this.getId());
        mentorship.setUuid(this.getUuid());
        mentorship.setCode(this.getCode());
        mentorship.setStartDate(this.getStartDate());
        mentorship.setEndDate(this.getEndDate());
        mentorship.setIterationNumber(this.getIterationNumber());

        if(this.getHealthFacility()!=null) {
            mentorship.setHealthFacility(this.getHealthFacility().getHealthFacilityObj());
        }
        if(this.getMentor()!=null) {
            mentorship.setTutor(this.getMentor().getTutor());
        }
        if(this.getMentee()!=null) {
            mentorship.setTutored(this.getMentee().getMentee());
        }
        if(this.getSession()!=null) {
            mentorship.setSession(this.getSession().getSession());
        }
        if(this.getForm()!=null) {
            mentorship.setForm(this.getForm().toForm());
        }
        if(this.getCabinet()!=null) {
            mentorship.setCabinet(this.getCabinet().getCabinet());
        }
        if(this.getDoor()!=null) {
            mentorship.setDoor(this.getDoor().toDoor());
        }
        if(this.getIterationType()!=null) {
            mentorship.setIterationType(this.getIterationType().toIterationType());
        }
        if(this.getTimeOfDay()!=null) {
            mentorship.setTimeOfDay(this.getTimeOfDay().toTimeOfDay());
        }
        return mentorship;
    }
}
