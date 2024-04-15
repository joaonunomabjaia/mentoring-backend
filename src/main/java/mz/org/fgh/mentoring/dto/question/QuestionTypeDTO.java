package mz.org.fgh.mentoring.dto.question;


import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.QuestionType;

import java.io.Serializable;

/**
 * @author Francisco da Conceicao Alberto Macuacua
 */
@Data
@AllArgsConstructor
public class QuestionTypeDTO extends BaseEntityDTO implements Serializable {
    private String description;
    private  String code;

    @Creator
    public QuestionTypeDTO(){}
    public QuestionTypeDTO(QuestionType questionType){
        super(questionType);
        this.description=questionType.getDescription();
        this.code = questionType.getCode();
    }
}
