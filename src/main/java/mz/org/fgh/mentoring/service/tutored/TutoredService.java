package mz.org.fgh.mentoring.service.tutored;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.repository.tutored.TutoredRepository;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class TutoredService {

    private final TutoredRepository tutoredRepository;
    public TutoredService(TutoredRepository tutoredRepository) {
        this.tutoredRepository = tutoredRepository;
    }

    public List<TutoredDTO> findAll(){
        List<Tutored> tutoreds = this.tutoredRepository.findAll();
        List<TutoredDTO> tutoredDTOS = new ArrayList<>(tutoreds.size());
        for (Tutored tutored :tutoreds) {
            tutoredDTOS.add(new TutoredDTO(tutored));
        }
        return tutoredDTOS;
    }
}
