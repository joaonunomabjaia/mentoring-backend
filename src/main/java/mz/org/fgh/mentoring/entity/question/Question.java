package mz.org.fgh.mentoring.entity.question;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "question")
@Table(name = "questions")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class Question  extends BaseEntity {


    @NotEmpty
    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @NotEmpty
    @Column(name = "QUESTION", nullable = false)
    private String question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "QUESTION_CATEGORY_ID")
    private QuestionCategory questionsCategory;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private Set<FormQuestion> formQuestions = new HashSet<>();

    @Creator
    public Question(){}
    public Question(QuestionDTO questionDTO){
        super(questionDTO);
        this.code=questionDTO.getCode();
        this.question=questionDTO.getQuestion();
        this.questionsCategory = new QuestionCategory(questionDTO.getQuestionCategory());
    }

    @Override
    public String toString() {
        return "Question{" +
                "code='" + code + '\'' +
                ", question='" + question + '\'' +
                ", questionsCategory=" + questionsCategory +
                '}';
    }
}
