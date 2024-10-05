package mz.org.fgh.mentoring.dto.question;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.Section;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class SectionDTO extends BaseEntityDTO implements Serializable {

    @NotEmpty(message = "Description cannot be empty")
    @Size(max = 100, message = "Description cannot be longer than 100 characters")
    private String description;

    @Creator
    public SectionDTO() {}

    public SectionDTO(Section section) {
        super(section);
        this.setDescription(section.getDescription());
    }
}
