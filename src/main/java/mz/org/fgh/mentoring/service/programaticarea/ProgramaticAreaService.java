package mz.org.fgh.mentoring.service.programaticarea;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.error.RecordInUseException;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.programaticarea.ProgramaticAreaRepository;
import mz.org.fgh.mentoring.repository.programaticarea.TutorProgrammaticAreaRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ProgramaticAreaService {

    private final ProgramaticAreaRepository programaticAreaRepository;

    private final TutorProgrammaticAreaRepository tutorProgrammaticAreaRepository;
    private final FormRepository formRepository;

    public ProgramaticAreaService(ProgramaticAreaRepository programaticAreaRepository, TutorProgrammaticAreaRepository tutorProgrammaticAreaRepository, FormRepository formRepository) {
        this.programaticAreaRepository = programaticAreaRepository;
        this.tutorProgrammaticAreaRepository = tutorProgrammaticAreaRepository;
        this.formRepository = formRepository;
    }


    public List<ProgrammaticAreaDTO> findProgrammaticAreas(final String code, final String name){
        List<ProgrammaticAreaDTO> programmaticAreaDTOS = new ArrayList<>();

        List<ProgrammaticArea> programmaticAreas = this.programaticAreaRepository.findBySelectedFilter(code, name, LifeCycleStatus.ACTIVE);
        for(ProgrammaticArea programmaticArea : programmaticAreas){
            programmaticAreaDTOS.add(new ProgrammaticAreaDTO(programmaticArea));
        }
        return programmaticAreaDTOS;
    }

    public List<ProgrammaticAreaDTO> findProgrammaticAreasByProgramId(final Long programId){

        List<ProgrammaticAreaDTO> programmaticAreas = new ArrayList<>();

        List<ProgrammaticArea> programmaticAreaList = this.programaticAreaRepository.findProgrammaticAreasByProgramId(programId);

        for (ProgrammaticArea programmaticArea : programmaticAreaList){
            programmaticAreas.add(new ProgrammaticAreaDTO(programmaticArea));
        }

        return programmaticAreas;
    }

    public Page fetchAllProgrammaticAreas(Pageable pageable){

        Page<ProgrammaticArea> programmaticAreaList = this.programaticAreaRepository.fetchAll(LifeCycleStatus.ACTIVE, pageable);

        return programmaticAreaList.map(this::programmaticAreaDTO);
    }

    private ProgrammaticAreaDTO programmaticAreaDTO(ProgrammaticArea programmaticArea){
        return new ProgrammaticAreaDTO(programmaticArea);
    }

    public ProgrammaticArea getProgrammaticAreaById(Long id) {
        return this.programaticAreaRepository.getById(id);
    }


    public Page<ProgrammaticArea> findAll(@Nullable Pageable pageable) {
        return programaticAreaRepository.findAll(pageable);
    }

    public Page<ProgrammaticArea> searchByName(String name, Pageable pageable) {
        return programaticAreaRepository.findByNameIlike("%" + name + "%", pageable);
    }

    @Transactional
    public ProgrammaticArea create(ProgrammaticArea area) {
        area.setUuid(java.util.UUID.randomUUID().toString());
        area.setDescription(area.getName());
        area.setCode(area.getName().toUpperCase());
        area.setCreatedAt(mz.org.fgh.mentoring.util.DateUtils.getCurrentDate());
        area.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        return programaticAreaRepository.save(area);
    }

    @Transactional
    public ProgrammaticArea update(ProgrammaticArea area) {
        ProgrammaticArea existing = programaticAreaRepository.findByUuid(area.getUuid())
                .orElseThrow(() -> new RuntimeException("Área programática não encontrada com UUID: " + area.getUuid()));

        existing.setName(area.getName());
        existing.setDescription(area.getDescription());
        existing.setProgram(area.getProgram());
        existing.setUpdatedAt(mz.org.fgh.mentoring.util.DateUtils.getCurrentDate());
        existing.setUpdatedBy(area.getUpdatedBy());

        return programaticAreaRepository.update(existing);
    }

    @Transactional
    public void delete(String uuid) {
        ProgrammaticArea area = programaticAreaRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Área programática não encontrada com UUID: " + uuid));

        // Verificações futuras aqui, se necessário
        long count = formRepository.countByProgrammaticArea(area) + tutorProgrammaticAreaRepository.countByProgrammaticArea(area);

         if (count > 0) throw new RecordInUseException("Área associada a Tabelas ou mentores");

        programaticAreaRepository.delete(area);
    }

    @Transactional
    public ProgrammaticArea updateLifeCycleStatus(String uuid, LifeCycleStatus status, String userUuid) {
        ProgrammaticArea area = programaticAreaRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Área programática não encontrada com UUID: " + uuid));

        area.setLifeCycleStatus(status);
        area.setUpdatedAt(mz.org.fgh.mentoring.util.DateUtils.getCurrentDate());
        area.setUpdatedBy(userUuid);

        return programaticAreaRepository.update(area);
    }
}
