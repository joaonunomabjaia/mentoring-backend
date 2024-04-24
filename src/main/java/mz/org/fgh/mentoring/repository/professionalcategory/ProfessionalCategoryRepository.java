package mz.org.fgh.mentoring.repository.professionalcategory;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;

import java.util.List;

@Repository
public interface ProfessionalCategoryRepository extends CrudRepository<ProfessionalCategory, Long> {

    List<ProfessionalCategory> findAll();

    ProfessionalCategory findByUuid(String uuid);

    @Query(value = "select * from professional_category limit :limit offset :offset ", nativeQuery = true)
    List<ProfessionalCategory> findProfessionalCategoriesWithLimit(Long limit, Long offset);
}
