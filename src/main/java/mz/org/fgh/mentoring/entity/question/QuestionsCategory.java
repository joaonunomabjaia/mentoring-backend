package mz.org.fgh.mentoring.entity.question;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "QUESTION_CATEGORIES")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class QuestionsCategory extends BaseEntity {

    @NotEmpty
    @Column(name = "CATEGORY", nullable = false, length = 100)
    private String category;
}
