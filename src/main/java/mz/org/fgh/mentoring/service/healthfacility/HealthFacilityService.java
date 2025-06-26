package mz.org.fgh.mentoring.service.healthfacility;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.healthFacility.HealthFacilityDTO;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.error.RecordInUseException;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.location.LocationRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.util.Utilities;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class HealthFacilityService {

    private final HealthFacilityRepository healthFacilityRepository;

    @Inject
    private TutorRepository tutorRepository;

    private final LocationRepository locationRepository;
    private final RondaRepository rondaRepository;

    public HealthFacilityService(HealthFacilityRepository healthFacilityRepository, LocationRepository locationRepository, RondaRepository rondaRepository) {
        this.healthFacilityRepository = healthFacilityRepository;
        this.locationRepository = locationRepository;
        this.rondaRepository = rondaRepository;
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

    public Page<HealthFacility> findAll(@Nullable Pageable pageable) {
        return healthFacilityRepository.findAll(pageable);
    }

    public Page<HealthFacility> searchByName(String name, Pageable pageable) {
        return healthFacilityRepository.findByHealthFacilityIlike("%" + name + "%", pageable);
    }

    @Transactional
    public HealthFacility create(HealthFacility facility) {
        facility.setUuid(java.util.UUID.randomUUID().toString());
        facility.setCreatedAt(mz.org.fgh.mentoring.util.DateUtils.getCurrentDate());
        facility.setLifeCycleStatus(mz.org.fgh.mentoring.util.LifeCycleStatus.ACTIVE);
        return healthFacilityRepository.save(facility);
    }

    @Transactional
    public HealthFacility update(HealthFacility facility) {
        HealthFacility existing = healthFacilityRepository.findByUuid(facility.getUuid())
                .orElseThrow(() -> new RuntimeException("Unidade sanitária não encontrada com UUID: " + facility.getUuid()));

        existing.setHealthFacility(facility.getHealthFacility());
        existing.setDistrict(facility.getDistrict());
        existing.setUpdatedAt(mz.org.fgh.mentoring.util.DateUtils.getCurrentDate());
        existing.setUpdatedBy(facility.getUpdatedBy());

        return healthFacilityRepository.update(existing);
    }

    @Transactional
    public HealthFacility updateLifeCycleStatus(String uuid, mz.org.fgh.mentoring.util.LifeCycleStatus status, String userUuid) {
        HealthFacility facility = healthFacilityRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Unidade sanitária não encontrada com UUID: " + uuid));

        facility.setLifeCycleStatus(status);
        facility.setUpdatedAt(mz.org.fgh.mentoring.util.DateUtils.getCurrentDate());
        facility.setUpdatedBy(userUuid);

        return healthFacilityRepository.update(facility);
    }

    @Transactional
    public void delete(String uuid) {
        HealthFacility facility = healthFacilityRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Unidade sanitária não encontrada com UUID: " + uuid));

        long used = locationRepository.countByHealthFacility(facility) + rondaRepository.countByHealthFacility(facility);
        // Verificações adicionais podem ser colocadas aqui se necessário
        if (used > 0) throw new RecordInUseException("Unidade sanitária associada a outros registos, impossível apagar.");

        healthFacilityRepository.delete(facility);
    }

}
