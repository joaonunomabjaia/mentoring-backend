package mz.org.fgh.mentoring.entity.question;


import io.micronaut.core.annotation.Creator;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.question.QuestionTypeDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "question_type")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class QuestionType  extends BaseEntity {

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "code", nullable = false)
    private  String code;

    @Creator
    public QuestionType(){}
    public QuestionType(QuestionTypeDTO questionTypeDTO) {
        super(questionTypeDTO);
        this.description= questionTypeDTO.getDescription();
        this.code= questionTypeDTO.getCode();
    }

    @Override
    public String toString() {
        return "QuestionType{" +
                "description='" + description + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
