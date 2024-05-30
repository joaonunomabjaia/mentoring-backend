package mz.org.fgh.mentoring.service.programaticarea;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.programaticarea.ProgramaticAreaRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

@Singleton
public class ProgramaticAreaService {

    private ProgramaticAreaRepository programaticAreaRepository;

    @Inject
    private UserRepository userRepository;

    public ProgramaticAreaService(ProgramaticAreaRepository programaticAreaRepository) {
        this.programaticAreaRepository = programaticAreaRepository;
    }

    public ProgrammaticArea createProgrammaticArea(final ProgrammaticArea programaticArea, Long userId){

         User user = userRepository.findById(userId).get();
         programaticArea.setCreatedBy(user.getUuid());
         programaticArea.setUuid(UUID.randomUUID().toString());
         programaticArea.setCreatedAt(DateUtils.getCurrentDate());
         programaticArea.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
   
        return this.programaticAreaRepository.save(programaticArea);
    }

    public ProgrammaticArea updateProgrammaticArea(final ProgrammaticArea programaticArea){

        return this.programaticAreaRepository.update(programaticArea);
    }

//    public List<ProgrammaticAreaDTO> fetchProgrammaticAreasAll(final Long limit,final Long offset){
//        List<ProgrammaticAreaDTO> programmaticAreaDTOS = new ArrayList<>();
//        List<ProgrammaticArea> programmaticAreas = new ArrayList<>();
//
//        if(limit==null || offset==null) {
//            programmaticAreas  = this.programaticAreaRepository.findAll();
//        } else if(limit > 0){
//            programmaticAreas = this.programaticAreaRepository.findProgrammaticAreaWithLimit(limit, offset);
//        }
//        else {
//            programmaticAreas  = this.programaticAreaRepository.findAll();
//        }
//
//        for(ProgrammaticArea programmaticArea : programmaticAreas){
//            programmaticAreaDTOS.add(new ProgrammaticAreaDTO(programmaticArea));
//        }
//        return programmaticAreaDTOS;
//    }

    public List<ProgrammaticAreaDTO> findProgrammaticAreas(final String code, final String name){
        List<ProgrammaticAreaDTO> programmaticAreaDTOS = new ArrayList<>();

        List<ProgrammaticArea> programmaticAreas = this.programaticAreaRepository.findBySelectedFilter(code, name, LifeCycleStatus.ACTIVE);
        for(ProgrammaticArea programmaticArea : programmaticAreas){
            programmaticAreaDTOS.add(new ProgrammaticAreaDTO(programmaticArea));
        }
        return programmaticAreaDTOS;
    }
//    public List<ProgrammaticAreaDTO> findProgrammaticAreaByTutorProgrammaticAreaUuid(final String tutorUuid){
//
//        List<ProgrammaticAreaDTO> programmaticAreaDTOS = new ArrayList<>();
//
//        List<ProgrammaticArea> programmaticAreas = this.programaticAreaRepository.findProgrammaticAreaByTutorProgrammaticAreaUuid(tutorUuid);
//
//        for (ProgrammaticArea programmaticArea : programmaticAreas){
//            programmaticAreaDTOS.add(new ProgrammaticAreaDTO(programmaticArea));
//        }
//
//        return programmaticAreaDTOS;
//    }

    public List<ProgrammaticAreaDTO> findProgrammaticAreasByProgram(final String program){

        List<ProgrammaticAreaDTO> programmaticAreas = new ArrayList<>();

        List<ProgrammaticArea> programmaticAreaList = this.programaticAreaRepository.findProgrammaticAreasByProgram(program);

        for (ProgrammaticArea programmaticArea : programmaticAreaList){
            programmaticAreas.add(new ProgrammaticAreaDTO(programmaticArea));
        }

        return programmaticAreas;
    }

    public List<ProgrammaticAreaDTO> fetchAllProgrammaticAreas(){

        List<ProgrammaticAreaDTO> programmaticAreas = new ArrayList<>();

        List<ProgrammaticArea> programmaticAreaList = this.programaticAreaRepository.fetchAll(LifeCycleStatus.ACTIVE);

        for (ProgrammaticArea programmaticArea : programmaticAreaList){
            programmaticAreas.add(new ProgrammaticAreaDTO(programmaticArea));
        }

        return programmaticAreas;
    }

    public ProgrammaticArea getProgrammaticAreaById(Long id) {
        return this.programaticAreaRepository.getById(id);
    }

    @Transactional
    public ProgrammaticArea delete(ProgrammaticArea programmaticArea, Long userId) {
        User user = userRepository.findById(userId).get();
        programmaticArea.setLifeCycleStatus(LifeCycleStatus.DELETED);
        programmaticArea.setUpdatedBy(user.getUuid());
        programmaticArea.setUpdatedAt(DateUtils.getCurrentDate());

        return this.programaticAreaRepository.update(programmaticArea);
    }
}
