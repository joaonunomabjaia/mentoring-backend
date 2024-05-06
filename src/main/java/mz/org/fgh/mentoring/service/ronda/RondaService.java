package mz.org.fgh.mentoring.service.ronda;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.repository.ronda.RondaRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class RondaService {

    private final RondaRepository rondaRepository;

    public RondaService(RondaRepository rondaRepository) {
        this.rondaRepository = rondaRepository;
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
        RondaDTO dto = new RondaDTO(createdRonda);
        return dto;
    }
}
