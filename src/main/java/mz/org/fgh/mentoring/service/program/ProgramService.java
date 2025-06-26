package mz.org.fgh.mentoring.service.program;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.error.RecordInUseException;
import mz.org.fgh.mentoring.repository.program.ProgramRepository;
import mz.org.fgh.mentoring.repository.programaticarea.ProgramaticAreaRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.programaticarea.ProgramaticAreaService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author Jose Julai Ritsure
 */
@Singleton
public class ProgramService {
    private final ProgramRepository programRepository;
    private final UserRepository userRepository;
    private final ProgramaticAreaRepository  programaticAreaRepository;

    @Inject
    private  ProgramaticAreaService programaticAreaService;
    public ProgramService(ProgramRepository programRepository, UserRepository userRepository, ProgramaticAreaRepository programaticAreaRepository) {
        this.programRepository = programRepository;
        this.userRepository = userRepository;
        this.programaticAreaRepository = programaticAreaRepository;
    }
    public Page findAllPrograms(Pageable pageable) {
        Page<Program> programList = this.programRepository.findAll(pageable);

        return programList.map(this::programDTO);
    }

    private ProgramDTO programDTO(Program program){
        return new ProgramDTO(program);
    }

    @Transactional
    public Program create(Program program) {
        program.setCreatedAt(DateUtils.getCurrentDate());
        program.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        program.setCode(program.getName().toUpperCase());
        return this.programRepository.save(program);
    }

    public Optional<Program> findById(final Long id){
        return this.programRepository.findById(id);
    }

    @Transactional
    public Program update(Program program) {
        Optional<Program> existing = programRepository.findByUuid(program.getUuid());
        if (existing.isEmpty()) {
            throw new RuntimeException("Program not found with UUID: " + program.getUuid());
        }

        Program toUpdate = existing.get();
        toUpdate.setName(program.getName());
        toUpdate.setDescription(program.getDescription());
        toUpdate.setUpdatedAt(DateUtils.getCurrentDate());
        toUpdate.setUpdatedBy(program.getUpdatedBy());

        return programRepository.update(toUpdate);
    }

    @Transactional
    public void delete(String uuid) {
        Optional<Program> existing = programRepository.findByUuid(uuid);
        if (existing.isEmpty()) {
            throw new RuntimeException("Program not found with UUID: " + uuid);
        }

        Program program = existing.get();

        // Adicione verificações de integridade se necessário
        long count = programaticAreaRepository.countByProgram(program);
        if (count > 0) { throw new RecordInUseException("O programa não pode ser eliminado porque está associado a outros registos."); }

        programRepository.delete(program);
    }


    @Transactional
    public Program delete(Program program, Long userId) {
        User user = userRepository.findById(userId).get();
        program.setLifeCycleStatus(LifeCycleStatus.DELETED);
        program.setUpdatedBy(user.getUuid());
        program.setUpdatedAt(DateUtils.getCurrentDate());

        return this.programRepository.update(program);
    }

    @Transactional
    public void destroy(Program program) {
        List<ProgrammaticAreaDTO> pas = programaticAreaService.findProgrammaticAreasByProgramId(program.getId());
        if(pas.isEmpty()) {
            programRepository.delete(program);
        }
    }

    public Page<Program> findAll(@Nullable Pageable pageable) {
        return programRepository.findAll(pageable);
    }

    public Page<Program> searchByName(String name, Pageable pageable) {
        return programRepository.findByNameIlike("%" + name + "%", pageable);
    }

    @Transactional
    public Program updateLifeCycleStatus(String uuid, LifeCycleStatus newStatus, String userId) {
        Optional<Program> existing = programRepository.findByUuid(uuid);
        if (existing.isEmpty()) {
            throw new RuntimeException("Program not found with UUID: " + uuid);
        }

        Program program = existing.get();
        program.setLifeCycleStatus(newStatus);
        program.setUpdatedAt(DateUtils.getCurrentDate());
        program.setUpdatedBy(userId);

        return programRepository.update(program);
    }

}
