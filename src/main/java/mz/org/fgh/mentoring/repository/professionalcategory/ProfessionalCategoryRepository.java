package mz.org.fgh.mentoring.repository.professionalcategory;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessionalCategoryRepository extends JpaRepository<ProfessionalCategory, Long> {


    @Query(value = "select * from professional_category limit :limit offset :offset ", nativeQuery = true)
    List<ProfessionalCategory> findProfessionalCategoriesWithLimit(Long limit, Long offset);

    Page<ProfessionalCategory> findByDescriptionIlike(String s, Pageable pageable);

    Optional<ProfessionalCategory> findByUuid(String uuid);
}
