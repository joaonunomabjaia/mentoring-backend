package mz.org.fgh.mentoring.service.location;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.location.CabinetDTO;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.location.CabinetRepository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class CabinetService {
    private final CabinetRepository cabinetRepository;

    public CabinetService(CabinetRepository cabinetRepository) {
        this.cabinetRepository = cabinetRepository;
    }

    public List<Cabinet> findAll() {
        return cabinetRepository.findAll();
    }

    public List<CabinetDTO> findAllCabinets() {
        List<Cabinet> cabinets = cabinetRepository.findAll();
        List<CabinetDTO> cabinetDTOS = new ArrayList<CabinetDTO>(0);
        for (Cabinet cabinet : cabinets) {
            CabinetDTO dto = new CabinetDTO(cabinet);
            cabinetDTOS.add(dto);
        }
        return cabinetDTOS;
    }

    public Cabinet findCabinetById(@NotNull Long id) {
        Optional<Cabinet> optCabinet = cabinetRepository.findById(id);
        if (optCabinet.isEmpty()) {
            throw new MentoringBusinessException("Cabinet with ID - " + id + " does not exist.");
        }
        return optCabinet.get();
    }

    public Cabinet createCabinet(Cabinet cabinet) {
        return cabinetRepository.save(cabinet);
    }
}
