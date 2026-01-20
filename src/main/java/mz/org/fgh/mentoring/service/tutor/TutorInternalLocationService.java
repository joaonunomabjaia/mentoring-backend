package mz.org.fgh.mentoring.service.tutor;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutor.TutorInternalLocation;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorInternalLocationRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class TutorInternalLocationService {

    private final TutorInternalLocationRepository repository;
    private final TutorRepository tutorRepository;
    private final HealthFacilityRepository healthFacilityRepository;
    private final UserRepository userRepository;

    public TutorInternalLocationService(
            TutorInternalLocationRepository repository,
            TutorRepository tutorRepository,
            HealthFacilityRepository healthFacilityRepository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.tutorRepository = tutorRepository;
        this.healthFacilityRepository = healthFacilityRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retorna a localização interna ATIVA do tutor
     */

    public Optional<TutorInternalLocation> findActiveByTutorUuid(String tutorUuid) {
        return repository.findActiveByTutorUuidWithHealthFacility(tutorUuid);
    }


    /**
     * Histórico completo
     */
    public List<TutorInternalLocation> findHistoryByTutorUuid(String tutorUuid) {
        Tutor tutor = tutorRepository.findByUuid(tutorUuid)
                .orElseThrow(() -> new IllegalArgumentException("Tutor not found"));

        return repository.findAllByTutorOrderByCreatedAtDesc(tutor);
    }


    /**
     * Atribui nova localização interna
     * - Inativa a anterior
     * - Cria um novo registo ACTIVE
     */
    @Transactional
    public TutorInternalLocation assignInternalLocation(
            String tutorUuid,
            String healthFacilityUuid,
            User user
    ) {

        Tutor tutor = tutorRepository.findByUuid(tutorUuid)
                .orElseThrow(() -> new IllegalArgumentException("Tutor not found"));

        HealthFacility facility = healthFacilityRepository.findByUuid(healthFacilityUuid)
                .orElseThrow(() -> new IllegalArgumentException("Health facility not found"));

        // 1️⃣ Fechar localização ativa anterior (se existir)
        repository.findByTutorAndLifeCycleStatus(tutor, LifeCycleStatus.ACTIVE)
                .ifPresent(active -> {
                    active.setLifeCycleStatus(LifeCycleStatus.INACTIVE);
                    active.setEndDate(DateUtils.getCurrentDate());
                    active.setUpdatedAt(DateUtils.getCurrentDate());
                    active.setUpdatedBy(user.getUuid());
                    repository.update(active);
                });

        // 2️⃣ Criar novo registo ACTIVE
        TutorInternalLocation entity = new TutorInternalLocation();
        entity.setTutor(tutor);
        entity.setLocation(facility);
        entity.setUuid(UUID.randomUUID().toString());
        entity.setStartDate(DateUtils.getCurrentDate());
        entity.setEndDate(null);
        entity.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        entity.setCreatedAt(DateUtils.getCurrentDate());
        entity.setCreatedBy(user.getUuid());

        return repository.save(entity);
    }


    /**
     * Remove a localização interna ATUAL (sem apagar histórico)
     */
    @Transactional
    public void removeInternalLocation(String tutorUuid, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        Tutor tutor = tutorRepository.findByUuid(tutorUuid)
                .orElseThrow(() -> new IllegalArgumentException("Tutor not found"));

        repository.findByTutorAndLifeCycleStatus(tutor, LifeCycleStatus.ACTIVE)
                .ifPresent(active -> {
                    active.setLifeCycleStatus(LifeCycleStatus.INACTIVE);
                    active.setEndDate(DateUtils.getCurrentDate());
                    active.setUpdatedAt(DateUtils.getCurrentDate());
                    active.setUpdatedBy(user.getUuid());
                    repository.update(active);
                });
    }

}
