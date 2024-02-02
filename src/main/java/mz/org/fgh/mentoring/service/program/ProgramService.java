package mz.org.fgh.mentoring.service.program;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.repository.program.ProgramRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jose Julai Ritsure
 */
@Singleton
public class ProgramService {
    private ProgramRepository programRepository;
    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
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
}
