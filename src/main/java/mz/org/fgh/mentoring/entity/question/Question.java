package mz.org.fgh.mentoring.entity.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "question")
@Table(name = "questions")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Question  extends BaseEntity {

    @NotEmpty
    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @NotEmpty
    @Column(name = "QUESTION", nullable = false)
    private String question;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_TYPE")
    private QuestionType questionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_CATEGORY_ID")
    private QuestionsCategory questionsCategory;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private Set<FormQuestion> formQuestions = new HashSet<>();

}
