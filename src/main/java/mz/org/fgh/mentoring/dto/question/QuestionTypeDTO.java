package mz.org.fgh.mentoring.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.QuestionType;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.io.Serializable;

import io.micronaut.core.annotation.Creator;

@Data
@AllArgsConstructor
public class QuestionTypeDTO extends BaseEntityDTO implements Serializable {

    private String description;

    private  String code;

    @Creator
    public QuestionTypeDTO(){}

    public QuestionTypeDTO(QuestionType questionType) {
        super(questionType);
        this.setCode(questionType.getCode());
        this.setDescription(questionType.getDescription());
    }
}
