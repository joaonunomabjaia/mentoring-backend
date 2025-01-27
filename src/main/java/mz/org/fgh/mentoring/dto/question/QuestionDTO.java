package mz.org.fgh.mentoring.dto.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Creator;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.repository.question.QuestionRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class QuestionDTO extends BaseEntityDTO implements Serializable {

    @NotEmpty(message = "Code cannot be empty")
    @Size(max = 50, message = "Code cannot exceed 50 characters")
    private String code;

    @NotEmpty(message = "Table Code cannot be empty")
    @Size(max = 50, message = "Table Code cannot exceed 50 characters")
    private String tableCode;

    @NotEmpty(message = "Question cannot be empty")
    @Size(max = 1000, message = "Question cannot exceed 1000 characters")
    private String question;

    @JsonProperty(value = "program")
    private ProgramDTO programDTO;

    @JsonProperty("used_in_form_section")
    private boolean usedInFormSection;

    @JsonProperty(value = "programUuid")
    private String programUuid;


    @Creator
    public QuestionDTO() {}

    public QuestionDTO(Question question) {
        super(question);
        this.setCode(question.getCode());
        this.setTableCode(question.getTableCode());
        this.setQuestion(question.getQuestion());
        this.usedInFormSection = usedInFormSection;
        this.setProgramUuid(question.getProgram().getUuid());
        if (question.getProgram() != null) {
            this.setProgramDTO(new ProgramDTO(question.getProgram()));
        }
    }

    public QuestionDTO(Question question, boolean usedInFormSection) {
        super(question);
        this.setCode(question.getCode());
        this.setTableCode(question.getTableCode());
        this.setQuestion(question.getQuestion());
        this.usedInFormSection = usedInFormSection;
        this.setProgramUuid(question.getProgram().getUuid());
        if (question.getProgram() != null) {
            this.setProgramDTO(new ProgramDTO(question.getProgram()));
        }
    }
}
