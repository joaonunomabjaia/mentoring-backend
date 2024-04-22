package mz.org.fgh.mentoring.service.healthfacility;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.healthFacility.HealthFacilityDTO;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.location.District;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.location.LocationRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import org.apache.commons.lang3.StringUtils;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class HealthFacilityService {

    private final HealthFacilityRepository healthFacilityRepository;

    @Inject
    private TutorRepository tutorRepository;

    private LocationRepository locationRepository;
    @Inject
    private UserRepository userRepository;
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

    public HealthFacility findById(Long id){
       return this.healthFacilityRepository.findById(id).get();
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

    public List<HealthFacility> getByDistricts(List<String> uuids) {
        return healthFacilityRepository.getAllOfDistrict(uuids);
    }
    @Transactional
    public HealthFacility create(HealthFacility healthFacility, Long userId) {
        User user = userRepository.findById(userId).get();
        healthFacility.setCreatedBy(user.getUuid());
        healthFacility.setUuid(UUID.randomUUID().toString());
        healthFacility.setCreatedAt(DateUtils.getCurrentDate());
        healthFacility.setLifeCycleStatus(LifeCycleStatus.ACTIVE);

        return this.healthFacilityRepository.save(healthFacility);
    }
    @Transactional
    public HealthFacility update(HealthFacility healthFacility, Long userId) {
        HealthFacility healthFacilityDB = findById(healthFacility.getId());
        User user = userRepository.findById(userId).get();
        healthFacilityDB.setUpdatedBy(user.getUuid());
        healthFacilityDB.setUpdatedAt(DateUtils.getCurrentDate());

        return this.healthFacilityRepository.update(healthFacilityDB);
    }
}
