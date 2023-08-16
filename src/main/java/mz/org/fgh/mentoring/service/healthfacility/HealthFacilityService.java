package mz.org.fgh.mentoring.service.healthfacility;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Singleton
public class HealthFacilityService {

    private final HealthFacilityRepository healthFacilityRepository;

    public HealthFacilityService(HealthFacilityRepository healthFacilityRepository) {
        this.healthFacilityRepository = healthFacilityRepository;
    }

    public HealthFacility createHealthfacility(HealthFacility healthFacility) {
        if (StringUtils.isEmpty(healthFacility.getHealthFacility()) && healthFacility.getDistrict() == null) {
            throw new MentoringBusinessException("Fields 'HEALTH FACILITY' and 'DISTRICT' are required.");
        }
        return healthFacilityRepository.save(healthFacility);
    }

    public HealthFacility findHealthFacilityById(@NotNull Long id) {
        Optional<HealthFacility> optionalHealthFacility = healthFacilityRepository.findById(id);
        if (optionalHealthFacility.isEmpty()) {
            throw new MentoringBusinessException("Health Facility with ID: " + id + " ws not found.");
        }
        return optionalHealthFacility.get();
    }

    public List<HealthFacility> findAllHealthFacilities() {
        return healthFacilityRepository.findAll();
    }
}
