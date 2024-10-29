package mz.org.fgh.mentoring.entity.mentorship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.answer.AnswerDTO;
import mz.org.fgh.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.question.EvaluationType;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.util.Utilities;

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
@Entity(name = "Mentorship")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TUTORED_ID", nullable = false)
    private Tutored tutored;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FORM_ID", nullable = false)
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SESSION_ID")
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CABINET_ID")
    private Cabinet cabinet;

    @ManyToOne(fetch = FetchType.EAGER)
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


    public Mentorship(String uuid) {
        super(uuid);
    }

    public Mentorship(MentorshipDTO mentorshipDTO) {
        super(mentorshipDTO);
        this.setStartDate(mentorshipDTO.getStartDate());
        this.setEndDate(mentorshipDTO.getEndDate());
        this.setIterationNumber(mentorshipDTO.getIterationNumber());
        this.setDemonstration(mentorshipDTO.isDemonstration());
        this.setDemonstrationDetails(mentorshipDTO.getDemonstrationDetails());
        this.setPerformedDate(mentorshipDTO.getPerformedDate());
        this.setTutor(new Tutor(mentorshipDTO.getMentorUuid()));
        this.setTutored(new Tutored(mentorshipDTO.getMenteeUuid()));
        this.setSession(new Session(mentorshipDTO.getSessionUuid()));
        this.setForm(new Form(mentorshipDTO.getFormUuid()));
        this.setCabinet(new Cabinet(mentorshipDTO.getCabinetUuid()));
        this.setDoor(new Door(mentorshipDTO.getDoorUuid()));
        this.setEvaluationType(new EvaluationType(mentorshipDTO.getEvaluationTypeUuid()));
        
        if(mentorshipDTO.getAnswers()!=null) {
            this.setAnswers(Utilities.parse(mentorshipDTO.getAnswers(),Answer.class));
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
                ", cabinet=" + cabinet +
                ", evaluationType=" + evaluationType +
                ", iterationNumber=" + iterationNumber +
                ", door=" + door +
                ", demonstration=" + demonstration +
                ", demonstrationDetails='" + demonstrationDetails + '\'' +
                '}';
    }

    @JsonIgnore
    public boolean isPatientEvaluation() {
        return this.evaluationType.isPatientEvaluation();
    }
}
