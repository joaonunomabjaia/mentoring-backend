package mz.org.fgh.mentoring.service.program;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.program.ProgramRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.programaticarea.ProgramaticAreaService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Jose Julai Ritsure
 */
@Singleton
public class ProgramService {
    private final ProgramRepository programRepository;
    private final UserRepository userRepository;
    @Inject
    private  ProgramaticAreaService programaticAreaService;
    public ProgramService(ProgramRepository programRepository, UserRepository userRepository) {
        this.programRepository = programRepository;
        this.userRepository = userRepository;
    }
    public Page findAllPrograms(Pageable pageable) {
        Page<Program> programList = this.programRepository.findAll(pageable);

        return programList.map(this::programDTO);
    }

    private ProgramDTO programDTO(Program program){
        return new ProgramDTO(program);
    }

    @Transactional
    public Program create(Program program, Long userId) {
        User user = userRepository.findById(userId).get();
        program.setCreatedBy(user.getUuid());
        program.setUuid(UUID.randomUUID().toString());
        program.setCreatedAt(DateUtils.getCurrentDate());
        program.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        program.setName(program.getName());
        program.setDescription(program.getDescription());

        return this.programRepository.save(program);
    }
    public Optional<Program> findById(final Long id){
        return this.programRepository.findById(id);
    }
    @Transactional
    public Program update(Program program, Long userId) {
        User user = userRepository.findById(userId).get();
        program.setUpdatedBy(user.getUuid());
        program.setUpdatedAt(DateUtils.getCurrentDate());
        program.setName(program.getName());
        program.setDescription(program.getDescription());

        return this.programRepository.update(program);
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
}
