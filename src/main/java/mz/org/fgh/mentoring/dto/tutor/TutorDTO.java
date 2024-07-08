package mz.org.fgh.mentoring.dto.tutor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.dto.tutorProgrammaticArea.TutorProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorDTO extends BaseEntityDTO {
    private EmployeeDTO employeeDTO;
    private List<TutorProgrammaticAreaDTO> tutorProgrammaticAreaDTOS;

    public TutorDTO(Tutor tutor) {
        super(tutor);
        if(tutor.getEmployee()!=null) this.setEmployeeDTO(new EmployeeDTO(tutor.getEmployee()));
        List<TutorProgrammaticArea> tutorProgrammaticAreaDTOList = tutor.getTutorProgrammaticAreas();
        try {
            if(Utilities.listHasElements(tutorProgrammaticAreaDTOList)) {
                this.setTutorProgrammaticAreaDTOS(setTutorProgrammaticAreas(tutor.getTutorProgrammaticAreas()));
            }
        } catch (Exception e) {
        }
    }

    private List<TutorProgrammaticAreaDTO> setTutorProgrammaticAreas(List<TutorProgrammaticArea> tutorProgrammaticAreas) {

        if (!Utilities.listHasElements(tutorProgrammaticAreas)) return null;

        List<TutorProgrammaticAreaDTO> tutorProgrammaticAreaDTOList = new ArrayList<>();

        for (TutorProgrammaticArea tutorProgrammaticArea : tutorProgrammaticAreas) {
            tutorProgrammaticAreaDTOList.add(new TutorProgrammaticAreaDTO(tutorProgrammaticArea));
        }
        return tutorProgrammaticAreaDTOList;
    }

    public Tutor getTutor() {
        Tutor tutor = new Tutor();
        tutor.setId(this.getId());
        tutor.setUuid(this.getUuid());
        tutor.setCreatedAt(this.getCreatedAt());
        tutor.setUpdatedAt(this.getUpdatedAt());
        tutor.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        if(this.getEmployeeDTO()!=null) {
            tutor.setEmployee(this.getEmployeeDTO().getEmployee());
        }
        return tutor;
    }
}