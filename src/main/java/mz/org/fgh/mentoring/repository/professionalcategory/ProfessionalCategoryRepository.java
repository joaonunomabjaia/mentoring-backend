package mz.org.fgh.mentoring.repository.professionalcategory;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.dto.professionalCategory.ProfessionalCategoryDTO;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;

import java.util.List;

@Repository
public interface ProfessionalCategoryRepository extends CrudRepository<ProfessionalCategory, Long> {

    List<ProfessionalCategory> findAll();
}
