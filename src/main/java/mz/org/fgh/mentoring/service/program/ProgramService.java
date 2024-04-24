package mz.org.fgh.mentoring.service.program;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.program.ProgramRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
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
    public ProgramService(ProgramRepository programRepository, UserRepository userRepository) {
        this.programRepository = programRepository;
        this.userRepository = userRepository;
    }
    public List<ProgramDTO> findAllPrograms() {
        List<Program> programList = this.programRepository.findAll();
        List<ProgramDTO> programs = new ArrayList<ProgramDTO>();
        for (Program program: programList) {
            ProgramDTO programDTO = new ProgramDTO(program);
            programs.add(programDTO);
        }
        return programs;
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
}
