package mz.org.fgh.mentoring.service.location;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.location.CabinetRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Singleton
public class CabinetService {
    private final CabinetRepository cabinetRepository;

    public CabinetService(CabinetRepository cabinetRepository) {
        this.cabinetRepository = cabinetRepository;
    }

    public List<Cabinet> findAllCabinets() {
     return cabinetRepository.findAll();
    }

    public Cabinet findCabinetById(@NotNull Long id) {
        Optional<Cabinet> optCabinet = cabinetRepository.findById(id);
        if(optCabinet.isEmpty()){
            throw new MentoringBusinessException("Cabinet with ID - "+id+" does not exist.");
        }
        return optCabinet.get();
    }

    public Cabinet createCabinet(Cabinet cabinet) {
        return cabinetRepository.save(cabinet);
    }
}
