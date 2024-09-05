package mz.org.fgh.mentoring.service.healthfacility;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.healthFacility.HealthFacilityDTO;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.location.LocationRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.ronda.RondaService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

@Singleton
public class HealthFacilityService {

    private final HealthFacilityRepository healthFacilityRepository;

    @Inject
    private TutorRepository tutorRepository;

    private LocationRepository locationRepository;
    @Inject
    private UserRepository userRepository;

    @Inject
    private RondaService rondaService;

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
            List<HealthFacility> healthFacilities = new ArrayList<>();
            if(limit!=null && offset!=null && limit>0) {
                healthFacilities = healthFacilityRepository.findHealthFacilitiesWithLimit(limit, offset);
            } else {
                healthFacilities = healthFacilityRepository.findAll();
            }
            return Utilities.parseList(healthFacilities, HealthFacilityDTO.class);
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
        User user = userRepository.findById(userId).get();
        healthFacility.setUpdatedBy(user.getUuid());
        healthFacility.setUpdatedAt(DateUtils.getCurrentDate());

        return this.healthFacilityRepository.update(healthFacility);
    }

    @Transactional
    public HealthFacility delete(HealthFacility healthFacility, Long userId) {
        User user = userRepository.findById(userId).get();
        healthFacility.setLifeCycleStatus(LifeCycleStatus.DELETED);
        healthFacility.setUpdatedBy(user.getUuid());
        healthFacility.setUpdatedAt(DateUtils.getCurrentDate());
        
        return this.healthFacilityRepository.update(healthFacility);
    }

    @Transactional
    public void destroy(HealthFacility healthFacility) {
        boolean hasRondas = rondaService.doesHealthFacilityHaveRondas(healthFacility);
        if(!hasRondas){
            this.healthFacilityRepository.delete(healthFacility);
        }
    }

    public Page<HealthFacilityDTO> getByPageAndSize(Pageable pageable) {
        Page<HealthFacility> pageHealthFacilities = this.healthFacilityRepository.findAll(pageable);

        List<HealthFacility> hfList = pageHealthFacilities.getContent();

        List<HealthFacilityDTO> healthFacilities = new ArrayList<HealthFacilityDTO>();
        for (HealthFacility healthFacility: hfList) {
            HealthFacilityDTO hfDTO = new HealthFacilityDTO(healthFacility);
            healthFacilities.add(hfDTO);
        }

        return pageHealthFacilities.map(this::hfToDTO);
    }

    private HealthFacilityDTO hfToDTO(HealthFacility healthFacility){
        return new HealthFacilityDTO(healthFacility);
    }
}
