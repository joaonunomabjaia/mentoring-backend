package mz.org.fgh.mentoring.service.mentorship;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.mentorship.TimeOfDayDTO;
import mz.org.fgh.mentoring.entity.mentorship.TimeOfDay;
import mz.org.fgh.mentoring.repository.mentorship.TimeOfDayRepository;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class TimeOfDayService {

    @Inject
    TimeOfDayRepository timeOfDayRepository;

    public List<TimeOfDayDTO> findAll(){
        List<TimeOfDayDTO> timeOfDayDTOS = new ArrayList<>();
        List<TimeOfDay> timeOfDays = this.timeOfDayRepository.findAll();
        for (TimeOfDay timeOfDay : timeOfDays) {
            TimeOfDayDTO timeOfDayDTO = new TimeOfDayDTO(timeOfDay);
            timeOfDayDTOS.add(timeOfDayDTO);
        }
        return timeOfDayDTOS;
    }

    public TimeOfDayDTO getByCode(String code) {
        TimeOfDay timeOfDay = timeOfDayRepository.getByCode(code);
        TimeOfDayDTO timeOfDayDTO = new TimeOfDayDTO(timeOfDay);
        return timeOfDayDTO;
    }
}
