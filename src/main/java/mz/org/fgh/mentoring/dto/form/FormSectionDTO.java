package mz.org.fgh.mentoring.dto.form;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.question.SectionDTO;
import mz.org.fgh.mentoring.entity.form.FormSection;
import mz.org.fgh.mentoring.entity.formQuestion.FormSectionQuestion;
import mz.org.fgh.mentoring.util.Utilities;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class FormSectionDTO extends BaseEntityDTO implements Serializable {


    private Long formId;

    @NotNull(message = "Section cannot be null")
    private SectionDTO section;

    private Integer sequence;

    private List<FormQuestionDTO> formQuestionDTOList;

    @Creator
    public FormSectionDTO() {}

    public FormSectionDTO(FormSection formSection) {
        super(formSection);
        this.formId = formSection.getForm().getId();
        this.section = new SectionDTO(formSection.getSection());
        this.sequence = formSection.getSequence();
        if (Utilities.listHasElements(formQuestionDTOList)) {
            this.formQuestionDTOList = new ArrayList<>();
            for (FormSectionQuestion fsq : formSection.getFormSectionQuestions()) {
                this.formQuestionDTOList.add(new FormQuestionDTO(fsq));
            }
        }
    }
}
