package mz.org.fgh.mentoring.service.healthfacility;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.healthFacility.HealthFacilityDTO;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.location.LocationRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.util.Utilities;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class HealthFacilityService {

    private final HealthFacilityRepository healthFacilityRepository;

    @Inject
    private TutorRepository tutorRepository;

    private LocationRepository locationRepository;

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

    public List<HealthFacilityDTO> findAllOfDistrict(Long districtId) {
        try {
            return Utilities.parseList(this.healthFacilityRepository.findByDistrictId(districtId), HealthFacilityDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public List<HealthFacilityDTO> getAll(Long limit, Long offset) {
        try {
            return Utilities.parseList(this.healthFacilityRepository.findAll(), HealthFacilityDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    public List<HealthFacilityDTO> findAllOfProvince(Long provinceId) {
        return null;
    }

    public HealthFacilityDTO findById(Long id){

        HealthFacility healthFacility = this.healthFacilityRepository.findById(id).get();
        return  new HealthFacilityDTO(healthFacility);
    }

    public List<HealthFacilityDTO> getAllOfMentor(String uuid, Long limit, Long offset) {
        Optional<Tutor> tutor = tutorRepository.findByUuid(uuid);
        List<HealthFacility> healthFacilities = new ArrayList<>();

        if (tutor.isPresent()) {
            for (Location location : tutor.get().getEmployee().getLocations()) {
                healthFacilities.addAll(healthFacilityRepository.findByDistrictId(location.getDistrict().getId()));
            }
        }
        try {
            return Utilities.parseList(healthFacilities, HealthFacilityDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
