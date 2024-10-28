package mz.org.fgh.mentoring.service.programaticarea;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.programaticarea.ProgramaticAreaRepository;
import mz.org.fgh.mentoring.repository.programaticarea.TutorProgrammaticAreaRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.form.FormService;
import mz.org.fgh.mentoring.service.tutor.TutorService;
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

    @Inject
    private FormService formService;

    @Inject
    private FormRepository formRepository;

    @Inject
    private TutorService tutorService;

    @Inject
    private TutorProgrammaticAreaRepository tutorProgrammaticAreaRepository;
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

    public ProgrammaticArea updateProgrammaticArea(final ProgrammaticArea programaticArea, Long userId){
     
        ProgrammaticArea programmaticAreaDB = this.getProgrammaticAreaById(programaticArea.getId());     
        User user = userRepository.findById(userId).get();
       
        programmaticAreaDB.setUpdatedBy(user.getUuid());
        programmaticAreaDB.setUpdatedAt(DateUtils.getCurrentDate());
        programmaticAreaDB.setCode(programaticArea.getCode());
        programmaticAreaDB.setDescription(programaticArea.getDescription());
        programmaticAreaDB.setName(programaticArea.getName());
        programmaticAreaDB.setProgram(programaticArea.getProgram());

        return this.programaticAreaRepository.update(programmaticAreaDB);
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

    public List<ProgrammaticAreaDTO> findProgrammaticAreasByProgramId(final Long programId){

        List<ProgrammaticAreaDTO> programmaticAreas = new ArrayList<>();

        List<ProgrammaticArea> programmaticAreaList = this.programaticAreaRepository.findProgrammaticAreasByProgramId(programId);

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

    @Transactional
    public void destroy(ProgrammaticArea programmaticArea) {
        List<Form> forms = formRepository.findFormByProgrammaticAreaId(programmaticArea.getId());
        List<TutorProgrammaticArea> tutorProgrammaticAreas = tutorProgrammaticAreaRepository.findByProgrammaticAreaId(programmaticArea.getId());
        if(forms.isEmpty() && tutorProgrammaticAreas.isEmpty()) {
            programaticAreaRepository.delete(programmaticArea);
        }
    }
}
