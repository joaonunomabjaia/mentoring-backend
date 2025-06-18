package mz.org.fgh.mentoring.repository.district;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.location.District;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    List<District> findByProvinceId(@NotNull Long provinceId);

    List<District> findByDescription(String designation);

    District findByUuid(String uuid);

    @Query(value = "select * from districts limit :lim offset :of ", nativeQuery = true)
    List<District> findDistrictsWithLimit(Long lim, Long of);

    Page<District> findByDescriptionIlike(String s, Pageable pageable);
}
