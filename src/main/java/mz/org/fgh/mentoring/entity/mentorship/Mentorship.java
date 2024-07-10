package mz.org.fgh.mentoring.entity.mentorship;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.answer.AnswerDTO;
import mz.org.fgh.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.question.EvaluationType;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Schema(name = "Mentorship", description = "The outcome of the provided mentoring to the tutored individuals")
@Entity(name = "mentorship")
@Table(name = "mentorships")
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Mentorship extends BaseEntity {

    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Column(name = "END_DATE", nullable = false)
    private Date endDate;

    @Column(name = "PERFORMED_DATE", nullable = false)
    private Date performedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TUTOR_ID", nullable = false)
    private Tutor tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TUTORED_ID", nullable = false)
    private Tutored tutored;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORM_ID", nullable = false)
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SESSION_ID")
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CABINET_ID")
    private Cabinet cabinet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITERATION_TYPE_ID", nullable = false)
    private EvaluationType evaluationType;

    @Column(name = "ITERATION_NUMBER")
    private Integer iterationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOOR_ID", nullable = false)
    private Door door;

    @Column(name = "DEMONSTRATION")
    private boolean demonstration;

    @Column(name = "DEMONSTRATION_DETAILS")
    private String demonstrationDetails;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mentorship")
    private List<Answer> answers;

    public Mentorship() {
    }

    public Mentorship(MentorshipDTO mentorshipDTO) {
        super(mentorshipDTO);
        this.setStartDate(this.getStartDate());
        this.setEndDate(this.getEndDate());
        this.setIterationNumber(this.getIterationNumber());
        this.setDemonstration(mentorshipDTO.isDemonstration());
        this.setDemonstrationDetails(mentorshipDTO.getDemonstrationDetails());
        this.setPerformedDate(mentorshipDTO.getPerformedDate());

        if(mentorshipDTO.getMentor()!=null) {
            this.setTutor(new Tutor(mentorshipDTO.getMentor()));
        }
        if(mentorshipDTO.getMentee()!=null) {
            this.setTutored(new Tutored(mentorshipDTO.getMentee()));
        }
        if(mentorshipDTO.getSession()!=null) {
            this.setSession(new Session(mentorshipDTO.getSession()));
        }
        if(mentorshipDTO.getForm()!=null) {
            this.setForm(new Form(mentorshipDTO.getForm()));
        }
        if(mentorshipDTO.getCabinet()!=null) {
            this.setCabinet(new Cabinet(mentorshipDTO.getCabinet()));
        }
        if(mentorshipDTO.getDoor()!=null) {
            this.setDoor(new Door(mentorshipDTO.getDoor()));
        }
        if(mentorshipDTO.getEvaluationType()!=null) {
            this.setEvaluationType(new EvaluationType(mentorshipDTO.getEvaluationType()));
        }
        if(mentorshipDTO.getAnswers()!=null) {
            List<Answer> answerList = new ArrayList<>();
            for (AnswerDTO answerDTO: mentorshipDTO.getAnswers()) {
                answerList.add(new Answer(answerDTO));
            }
            this.setAnswers(answerList);
        }
    }

    @Override
    public String toString() {
        return "Mentorship{" +
                "endDate=" + endDate +
                ", startDate=" + startDate +
                ", performedDate=" + performedDate +
                ", tutor=" + tutor +
                ", tutored=" + tutored +
                ", form=" + form +
                ", session=" + session +
                ", cabinet=" + cabinet +
                ", evaluationType=" + evaluationType +
                ", iterationNumber=" + iterationNumber +
                ", door=" + door +
                ", demonstration=" + demonstration +
                ", demonstrationDetails='" + demonstrationDetails + '\'' +
                '}';
    }
}
