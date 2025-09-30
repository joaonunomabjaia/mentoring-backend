package mz.org.fgh.mentoring.service.mentorship;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.mentorship.EvaluationLocation;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.mentorship.EvaluationLocationRepository;
import mz.org.fgh.mentoring.service.user.UserService;
import mz.org.fgh.mentoring.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Singleton
public class EvaluationLocationService {

    private static final Logger LOG = LoggerFactory.getLogger(EvaluationLocationService.class);
    private final EvaluationLocationRepository repository;
    private final UserService userService;

    public EvaluationLocationService(EvaluationLocationRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public Page<EvaluationLocation> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<EvaluationLocation> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<EvaluationLocation> findByCode(String code) {
        return repository.findByCode(code);
    }

    public EvaluationLocation create(EvaluationLocation evaluationLocation, Long userId) {
        // Fetch user UUID based on the userId
        User user = userService.findById(userId); // Replace this with actual service/repository call
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        evaluationLocation.setCreatedBy(user.getUuid()); // Use userUuid for the UpdatedBy field
        evaluationLocation.setCreatedAt(DateUtils.getCurrentDate());
        EvaluationLocation savedLocation = repository.save(evaluationLocation);
        LOG.info("Created EvaluationLocation: {}", savedLocation);
        return savedLocation;
    }

    public EvaluationLocation update(EvaluationLocation evaluationLocation, Long userId) {
        // Fetch user UUID based on the userId
        User user = userService.findById(userId); // Replace this with actual service/repository call
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        evaluationLocation.setUpdatedBy(user.getUuid()); // Use userUuid for the UpdatedBy field
        evaluationLocation.setUpdatedAt(DateUtils.getCurrentDate());
        EvaluationLocation updatedLocation = repository.update(evaluationLocation);
        LOG.info("Updated EvaluationLocation: {}", updatedLocation);
        return updatedLocation;
    }


    public EvaluationLocation delete(EvaluationLocation evaluationLocation, Long userId) {
        EvaluationLocation deletedLocation = repository.update(evaluationLocation);
        LOG.info("Soft-deleted EvaluationLocation: {}", deletedLocation);
        return deletedLocation;
    }

    public void destroy(EvaluationLocation evaluationLocation) {
        repository.delete(evaluationLocation);
        LOG.info("Permanently deleted EvaluationLocation: {}", evaluationLocation);
    }
}
