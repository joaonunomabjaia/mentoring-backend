package mz.org.fgh.mentoring.service.programaticarea;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.repository.programaticarea.ProgramaticAreaRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.List;

@Singleton
public class ProgramaticAreaService {

    private ProgramaticAreaRepository programaticAreaRepository;

    public ProgramaticAreaService(ProgramaticAreaRepository programaticAreaRepository) {
        this.programaticAreaRepository = programaticAreaRepository;
    }

    public ProgrammaticArea createProgrammaticArea(final ProgrammaticArea programaticArea){

        return this.programaticAreaRepository.save(programaticArea);
    }

    public ProgrammaticArea updateProgrammaticArea(final ProgrammaticArea programaticArea){

        return this.programaticAreaRepository.update(programaticArea);
    }

    public List<ProgrammaticArea> findProgrammaticAreasAll(){
        return this.programaticAreaRepository.findAll();
    }

    public List<ProgrammaticArea> findProgrammaticAreas(final String code, final String name){
        return this.programaticAreaRepository.findBySelectedFilter(code, name, LifeCycleStatus.ACTIVE);
    }
}
