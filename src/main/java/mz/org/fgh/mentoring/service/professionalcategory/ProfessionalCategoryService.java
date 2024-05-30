package mz.org.fgh.mentoring.service.professionalcategory;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.professionalCategory.ProfessionalCategoryDTO;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.professionalcategory.ProfessionalCategoryRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class ProfessionalCategoryService {

    private final ProfessionalCategoryRepository professionalCategoryRepository;
    private final UserRepository userRepository;

    public ProfessionalCategoryService(ProfessionalCategoryRepository professionalCategoryRepository, UserRepository userRepository) {
        this.professionalCategoryRepository = professionalCategoryRepository;
        this.userRepository = userRepository;
    }

    public List<ProfessionalCategoryDTO> getAll(Long limit, Long offset) {
        try {
            List<ProfessionalCategory> professionalCategories = new ArrayList<>();
            if(limit!=null && offset!=null && limit>0) {
                professionalCategories = professionalCategoryRepository.findProfessionalCategoriesWithLimit(limit, offset);
            } else {
                professionalCategories = professionalCategoryRepository.findAll();
            }
            return Utilities.parseList(professionalCategories, ProfessionalCategoryDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    public ProfessionalCategoryDTO getById(Long id){

        ProfessionalCategory professionalCategory = this.professionalCategoryRepository.findById(id).get();

        return new ProfessionalCategoryDTO(professionalCategory);
    }

    @Transactional
    public ProfessionalCategory create(ProfessionalCategory professionalCategory, Long userId) {
        User user = userRepository.findById(userId).get();
        professionalCategory.setCreatedBy(user.getUuid());
        professionalCategory.setUuid(UUID.randomUUID().toString());
        professionalCategory.setCreatedAt(DateUtils.getCurrentDate());
        professionalCategory.setLifeCycleStatus(LifeCycleStatus.ACTIVE);

        return this.professionalCategoryRepository.save(professionalCategory);
    }
    public Optional<ProfessionalCategory> findById(final Long id){
        return this.professionalCategoryRepository.findById(id);
    }
    @Transactional
    public ProfessionalCategory update(ProfessionalCategory professionalCategory, Long userId) {
        User user = userRepository.findById(userId).get();
        professionalCategory.setUpdatedBy(user.getUuid());
        professionalCategory.setUpdatedAt(DateUtils.getCurrentDate());

        return this.professionalCategoryRepository.update(professionalCategory);
    }

    @Transactional
    public ProfessionalCategory delete(ProfessionalCategory professionalCategory, Long userId) {
        User user = userRepository.findById(userId).get();
        professionalCategory.setLifeCycleStatus(LifeCycleStatus.DELETED);
        professionalCategory.setUpdatedBy(user.getUuid());
        professionalCategory.setUpdatedAt(DateUtils.getCurrentDate());

        return this.professionalCategoryRepository.update(professionalCategory);
    }
}
