package mz.org.fgh.mentoring.service.mentorship;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.mentorship.DoorDTO;
import mz.org.fgh.mentoring.entity.mentorship.Door;
import mz.org.fgh.mentoring.repository.mentorship.DoorRepository;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class DoorService {

    @Inject
    DoorRepository doorRepository;

    public List<DoorDTO> findAll(){
        List<DoorDTO> doorDTOS = new ArrayList<>();
        List<Door> doors = this.doorRepository.findAll();
        for (Door door : doors) {
            DoorDTO doorDTO = new DoorDTO(door);
            doorDTOS.add(doorDTO);
        }
        return doorDTOS;
    }

    public DoorDTO getByCode(String code) {
        Door door = doorRepository.getByCode(code);
        DoorDTO doorDTO = new DoorDTO(door);
        return doorDTO;
    }
}
