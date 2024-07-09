package mz.org.fgh.mentoring.dto.mentorship;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.answer.AnswerDTO;
import mz.org.fgh.mentoring.dto.form.FormDTO;
import mz.org.fgh.mentoring.dto.healthFacility.HealthFacilityDTO;
import mz.org.fgh.mentoring.dto.location.CabinetDTO;
import mz.org.fgh.mentoring.dto.question.EvaluationTypeDTO;
import mz.org.fgh.mentoring.dto.session.SessionDTO;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.mentorship.Door;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.question.EvaluationType;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

@Data
@NoArgsConstructor
public class MentorshipDTO extends BaseEntityDTO {
    private Integer iterationNumber;
    private Date startDate;
    private Date endDate;
    private TutorDTO mentor;
    private TutoredDTO mentee;
    private SessionDTO session;
    private FormDTO form;
    private CabinetDTO cabinet;
    private DoorDTO door;
    private EvaluationTypeDTO evaluationType;
    private List<AnswerDTO> answers;
    private boolean demonstration;
    private String demonstrationDetails;
    public MentorshipDTO(Mentorship mentorship) {
        super(mentorship);
        this.setStartDate(mentorship.getStartDate());
        this.setEndDate(mentorship.getEndDate());
        this.setIterationNumber(mentorship.getIterationNumber());
        this.setDemonstration(mentorship.isDemonstration());
        this.setDemonstrationDetails(mentorship.getDemonstrationDetails());
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
        if(mentorship.getEvaluationType()!=null) {
            this.setEvaluationType(new EvaluationTypeDTO(mentorship.getEvaluationType()));
        }
        if(mentorship.getAnswers()!=null) {
            List<AnswerDTO> answerDTOS = new ArrayList<>();
            for (Answer answer: mentorship.getAnswers()) {
                answerDTOS.add(new AnswerDTO(answer));
            }
            this.setAnswers(answerDTOS);
        }
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

    public EvaluationTypeDTO getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(EvaluationTypeDTO evaluationType) {
        this.evaluationType = evaluationType;
    }

    public boolean isDemonstration() {
        return demonstration;
    }

    public void setDemonstration(boolean demonstration) {
        this.demonstration = demonstration;
    }

    public String getDemonstrationDetails() {
        return demonstrationDetails;
    }

    public void setDemonstrationDetails(String demonstrationDetails) {
        this.demonstrationDetails = demonstrationDetails;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    public Mentorship getMentorship() {
        Mentorship mentorship = new Mentorship();
        mentorship.setUuid(this.getUuid());
        mentorship.setId(this.getId());
        mentorship.setCreatedAt(this.getCreatedAt());
        mentorship.setUpdatedAt(this.getUpdatedAt());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) mentorship.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        mentorship.setStartDate(this.getStartDate());
        mentorship.setEndDate(this.getEndDate());
        mentorship.setIterationNumber(this.getIterationNumber());
        mentorship.setDemonstration(this.isDemonstration());
        mentorship.setDemonstrationDetails(this.getDemonstrationDetails());

        if(this.getMentor()!=null) {
            mentorship.setTutor(new Tutor(this.getMentor()));
        }
        if(this.getMentee()!=null) {
            mentorship.setTutored(new Tutored(this.getMentee()));
        }
        if(this.getSession()!=null) {
            mentorship.setSession(new Session(this.getSession()));
        }
        if(this.getForm()!=null) {
            mentorship.setForm(new Form(this.getForm()));
        }
        if(this.getCabinet()!=null) {
            mentorship.setCabinet(new Cabinet(this.getCabinet()));
        }
        if(this.getDoor()!=null) {
            mentorship.setDoor(new Door(this.getDoor()));
        }
        if(this.getEvaluationType()!=null) {
            mentorship.setEvaluationType(new EvaluationType(this.getEvaluationType()));
        }
        if(this.getAnswers()!=null) {
            List<Answer> answerList = new ArrayList<>();
            for (AnswerDTO answerDTO: this.getAnswers()) {
                answerList.add(new Answer(answerDTO));
            }
            mentorship.setAnswers(answerList);
        }
        return mentorship;
    }
}
