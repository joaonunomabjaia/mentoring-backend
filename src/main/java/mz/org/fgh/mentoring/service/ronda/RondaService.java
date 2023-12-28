package mz.org.fgh.mentoring.service.ronda;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.repository.ronda.RondaRepository;

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
}
