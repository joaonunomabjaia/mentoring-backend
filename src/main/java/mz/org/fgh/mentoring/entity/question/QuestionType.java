package mz.org.fgh.mentoring.entity.question;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "question_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionType  extends BaseEntity {
    private String description;

    private  String code;

}
