package mz.org.fgh.mentoring.service.ronda;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.ronda.RondaType;
import mz.org.fgh.mentoring.repository.ronda.RondaTypeRepository;

import java.util.List;
import java.util.Optional;

@Singleton
public class RondaTypeService {

    private final RondaTypeRepository rondaTypeRepository;

    public RondaTypeService(RondaTypeRepository rondaTypeRepository) {
        this.rondaTypeRepository = rondaTypeRepository;
    }

    public List<RondaType> findAll(){
        return this.rondaTypeRepository.findAll();
    }

    public Optional<RondaType> findById(final Long id){
        return this.rondaTypeRepository.findById(id);
    }

    public Optional<RondaType> findByUuid(final String uuid){
      return this.rondaTypeRepository.findByUuid(uuid);
    }

    public RondaType findByCode(final String code){
        return this.rondaTypeRepository.findByCode(code);
    }

}
