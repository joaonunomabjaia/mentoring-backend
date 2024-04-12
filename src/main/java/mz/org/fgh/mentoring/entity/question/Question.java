package mz.org.fgh.mentoring.entity.question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.util.HashSet;
import java.util.Set;
@Schema(name = "Question", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "Question")
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

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_CATEGORY_ID")
    private QuestionsCategory questionsCategory;

}
