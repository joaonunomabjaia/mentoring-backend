package mz.org.fgh.mentoring.dto.question;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.Question;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author Francisco da Conceicao Alberto Macuacua
 */
@Data
@AllArgsConstructor
public class QuestionDTO extends BaseEntityDTO implements Serializable {
    private String code;
    private String question;
    private QuestionCategoryDTO questionCategory;

    @Creator
    public QuestionDTO(){}
    public QuestionDTO(Question question) {
        super(question);
        this.code = question.getCode();
        this.question = question.getQuestion();
        this.questionCategory = new QuestionCategoryDTO(question.getQuestionsCategory());
    }
}
