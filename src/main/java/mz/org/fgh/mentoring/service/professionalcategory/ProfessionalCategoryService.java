package mz.org.fgh.mentoring.service.professionalcategory;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.professionalCategory.ProfessionalCategoryDTO;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.error.RecordInUseException;
import mz.org.fgh.mentoring.repository.employee.EmployeeRepository;
import mz.org.fgh.mentoring.repository.professionalcategory.ProfessionalCategoryRepository;
import mz.org.fgh.mentoring.service.employee.EmployeeService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class ProfessionalCategoryService {

    private final ProfessionalCategoryRepository professionalCategoryRepository;
    private final EmployeeRepository employeeRepository;

    @Inject
    private EmployeeService employeeService;

    public ProfessionalCategoryService(ProfessionalCategoryRepository professionalCategoryRepository, EmployeeRepository employeeRepository) {
        this.professionalCategoryRepository = professionalCategoryRepository;
        this.employeeRepository = employeeRepository;
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
    public ProfessionalCategory create(ProfessionalCategory professionalCategory) {
        professionalCategory.setCreatedAt(DateUtils.getCurrentDate());
        professionalCategory.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        professionalCategory.setCode(professionalCategory.getDescription().toUpperCase());
        return this.professionalCategoryRepository.save(professionalCategory);
    }

    public Optional<ProfessionalCategory> findById(final Long id){
        return this.professionalCategoryRepository.findById(id);
    }

    public Page<ProfessionalCategory> findAll(@Nullable Pageable pageable) {
        return professionalCategoryRepository.findAll(pageable);
    }

    public Page<ProfessionalCategory> searchByName(String name, Pageable pageable) {
        return professionalCategoryRepository.findByDescriptionIlike("%" + name + "%", pageable);
    }

    @Transactional
    public ProfessionalCategory updateLifeCycleStatus(String uuid, LifeCycleStatus newStatus, String userUuid) {
        Optional<ProfessionalCategory> existing = professionalCategoryRepository.findByUuid(uuid);
        if (existing.isEmpty()) {
            throw new RuntimeException("Categoria profissional não encontrada com UUID: " + uuid);
        }

        ProfessionalCategory category = existing.get();
        category.setLifeCycleStatus(newStatus);
        category.setUpdatedAt(DateUtils.getCurrentDate());
        category.setUpdatedBy(userUuid);

        return professionalCategoryRepository.update(category);
    }

    @Transactional
    public ProfessionalCategory update(ProfessionalCategory category) {
        Optional<ProfessionalCategory> existing = professionalCategoryRepository.findByUuid(category.getUuid());
        if (existing.isEmpty()) {
            throw new RuntimeException("Categoria profissional não encontrada com UUID: " + category.getUuid());
        }

        ProfessionalCategory toUpdate = existing.get();
        toUpdate.setDescription(category.getDescription());
        toUpdate.setUpdatedAt(DateUtils.getCurrentDate());
        toUpdate.setUpdatedBy(category.getUpdatedBy());

        return professionalCategoryRepository.update(toUpdate);
    }

    @Transactional
    public void delete(String uuid) {
        Optional<ProfessionalCategory> existing = professionalCategoryRepository.findByUuid(uuid);
        if (existing.isEmpty()) {
            throw new RuntimeException("Categoria profissional não encontrada com UUID: " + uuid);
        }

        ProfessionalCategory category = existing.get();

        // Exemplo de verificação futura:
         long count = employeeRepository.countByProfessionalCategory(category);
         if (count > 0) {
             throw new RecordInUseException("A categoria não pode ser eliminada porque está associada a outros registos.");
         }

        professionalCategoryRepository.delete(category);
    }

}
