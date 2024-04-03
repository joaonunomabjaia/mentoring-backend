package mz.org.fgh.mentoring.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.QuestionType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionTypeDTO extends BaseEntityDTO implements Serializable {

    private String description;

    private  String code;

    public QuestionTypeDTO(QuestionType questionType) {
        super(questionType);
        this.description = questionType.getDescription();
        this.code = questionType.getCode();
    }

    public QuestionType toQuestionType() {
        QuestionType questionType = new QuestionType();
        questionType.setCode(this.getCode());
        questionType.setId(this.getId());
        questionType.setDescription(this.getDescription());
        questionType.setUuid(this.getUuid());
        questionType.setLifeCycleStatus(this.getLifeCycleStatus());
        return questionType;
    }
}
