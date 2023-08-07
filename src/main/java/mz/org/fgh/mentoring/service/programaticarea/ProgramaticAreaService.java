package mz.org.fgh.mentoring.service.programaticarea;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.programaticarea.ProgramaticArea;
import mz.org.fgh.mentoring.repository.programaticarea.ProgramaticAreaRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.List;

@Singleton
public class ProgramaticAreaService {

    private ProgramaticAreaRepository programaticAreaRepository;

    public ProgramaticAreaService(ProgramaticAreaRepository programaticAreaRepository) {
        this.programaticAreaRepository = programaticAreaRepository;
    }

    public ProgramaticArea createProgrammaticArea(final ProgramaticArea programaticArea){

        return this.programaticAreaRepository.save(programaticArea);
    }

    public ProgramaticArea updateProgrammaticArea(final ProgramaticArea programaticArea){

        return this.programaticAreaRepository.update(programaticArea);
    }

    public List<ProgramaticArea> findProgrammaticAreasAll(){
        return this.programaticAreaRepository.findAll();
    }

    public List<ProgramaticArea> findProgrammaticAreas(final String code, final String name){
        return this.programaticAreaRepository.findBySelectedFilter(code, name, LifeCycleStatus.ACTIVE);
    }
}
