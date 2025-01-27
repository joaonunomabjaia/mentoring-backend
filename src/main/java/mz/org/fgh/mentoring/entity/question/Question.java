package mz.org.fgh.mentoring.entity.question;

import io.swagger.v3.oas.annotations.media.Schema;
import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.program.Program;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Schema(name = "Question", description = "Used to evaluate the mentee")
@Entity(name = "Question")
@Table(name = "questions")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class Question extends BaseEntity {

    @NotEmpty(message = "Code cannot be empty")
    @Size(max = 50, message = "Code cannot exceed 50 characters")
    @Column(name = "CODE", nullable = false, unique = true, length = 50)
    private String code;

    @NotEmpty(message = "Code cannot be empty")
    @Size(max = 50, message = "Code cannot exceed 50 characters")
    @Column(name = "TABLE_CODE", nullable = false, length = 50)
    private String tableCode;

    @NotEmpty(message = "Question cannot be empty")
    @Size(max = 1000, message = "Question cannot exceed 1000 characters")
    @Column(name = "QUESTION", nullable = false, length = 1000)
    private String question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROGRAM_ID", nullable = false)
    private Program program;

    @Creator
    public Question() {}
    public Question(String uuid) {
        super(uuid);
    }

    public Question(QuestionDTO questionDTO) {
        super(questionDTO);
        this.setCode(questionDTO.getCode());
        this.setTableCode(questionDTO.getTableCode());
        this.setQuestion(questionDTO.getQuestion());
        if (questionDTO.getProgramDTO() != null) this.setProgram(new Program(questionDTO.getProgramDTO()));
    }

    @Override
    public String toString() {
        return "Question{" +
                "code='" + code + '\'' +
                ", tableCode='" + tableCode + '\'' +
                ", question='" + question + '\'' +
                ", program=" + program +
                '}';
    }
}
