package mz.org.fgh.mentoring.service.indicator;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.indicator.Indicator;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.indicator.IndicatorRepository;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Singleton
public class IndicatorService {

    private final IndicatorRepository indicatorRepository;

    public IndicatorService(IndicatorRepository indicatorRepository){
        this.indicatorRepository = indicatorRepository;
    }

    public Indicator createIndicator(Indicator indicator){
        if(StringUtils.isEmpty(indicator.getCode()) && indicator.getForm() == null && indicator.getTutor() == null && indicator.getHealthFacility() == null){
            throw new MentoringBusinessException("Fields 'CODE', 'TUTOR', 'HEALTH FACILITY' and 'FORM' are required.");
        }
        return indicatorRepository.save(indicator);
    }

    public Indicator findIndicatorById(@NotNull Long id){
        Optional<Indicator> optionalIndicator = indicatorRepository.findById(id);
        if(optionalIndicator.isEmpty()){
            throw new MentoringBusinessException("Indicator with ID: "+id+" was not found.");
        }
        return optionalIndicator.get();
    }

    public List<Indicator> findAllIndicators(){
        return indicatorRepository.findAll();
    }
}
