package mz.org.fgh.mentoring.service.ronda;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.dto.ronda.RondaMenteeDTO;
import mz.org.fgh.mentoring.dto.ronda.RondaMentorDTO;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaMentee;
import mz.org.fgh.mentoring.entity.ronda.RondaMentor;
import mz.org.fgh.mentoring.repository.ronda.RondaMenteeRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaMentorRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class RondaService {

    private final RondaRepository rondaRepository;
    private final RondaMentorRepository rondaMentorRepository;
    private final RondaMenteeRepository rondaMenteeRepository;

    public RondaService(RondaRepository rondaRepository, RondaMentorRepository rondaMentorRepository, RondaMenteeRepository rondaMenteeRepository) {
        this.rondaRepository = rondaRepository;
        this.rondaMentorRepository = rondaMentorRepository;
        this.rondaMenteeRepository = rondaMenteeRepository;
    }

    public List<Ronda> findAll(){
        return this.rondaRepository.findAll();
    }

    public Optional<Ronda> findById(final Long id){
        return this.rondaRepository.findById(id);
    }

    public Optional<Ronda> findByUuid(final String uuid){
      return this.rondaRepository.findByUuid(uuid);
    }

    public List<Ronda> findRondaWithLimit(long limit, long offset){
        return this.rondaRepository.findRondaWithLimit(limit, offset);
    }

    public Ronda createRonda(final Ronda ronda){
        return this.rondaRepository.save(ronda);
    }

    public List<RondaDTO> getAllRondasOfMentor(Long mentorId) {
        List<Ronda> rondaList = this.rondaRepository.getAllRondasOfMentor(mentorId, LifeCycleStatus.BLOCKED);
        List<RondaDTO> rondas = new ArrayList<>();
        for (Ronda ronda: rondaList) {
            RondaDTO rondaDTO = new RondaDTO(ronda);
            rondas.add(rondaDTO);
        }
        return rondas;
    }

    public List<RondaDTO> createRondas(List<RondaDTO> rondaDTOS) {
        List<RondaDTO> rondas = new ArrayList<>();
        for (RondaDTO rondaDTO: rondaDTOS) {
            RondaDTO dto = this.createRonda(rondaDTO);
            rondas.add(dto);
        }
      return rondas;
    }

    public RondaDTO createRonda(RondaDTO rondaDTO) {
        Ronda ronda = rondaDTO.getRonda();
        Ronda createdRonda = this.rondaRepository.save(ronda);
        if(Utilities.listHasElements(rondaDTO.getRondaMentors())) {
            List<RondaMentor> rondaMentors = ronda.getRondaMentors();
            List<RondaMentor> savedRondaMentors = new ArrayList<>();
            for (RondaMentor rondaMentor: rondaMentors) {
                rondaMentor.setRonda(createdRonda);
                RondaMentor saveRondaMentor = this.rondaMentorRepository.save(rondaMentor);
                savedRondaMentors.add(saveRondaMentor);
            }
            createdRonda.setRondaMentors(savedRondaMentors);
        }
        if(Utilities.listHasElements(rondaDTO.getRondaMentees())) {
            List<RondaMentee> rondaMentees = ronda.getRondaMentees();
            List<RondaMentee> savedRondaMentees = new ArrayList<>();
            for (RondaMentee rondaMentee: rondaMentees) {
                rondaMentee.setRonda(createdRonda);
                RondaMentee savedRondaMentee = this.rondaMenteeRepository.save(rondaMentee);
                savedRondaMentees.add(savedRondaMentee);
            }
            createdRonda.setRondaMentees(savedRondaMentees);
        }
        RondaDTO dto = new RondaDTO(createdRonda);
        return dto;
    }

    public List<Ronda> getAllOfMentor(String mentorUuid) {
        return this.rondaRepository.getAllOfMentor(mentorUuid);
    }
}
