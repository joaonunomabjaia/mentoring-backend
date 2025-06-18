package mz.org.fgh.mentoring.repository.province;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.location.Province;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    List<Province> findByDesignation(String designation);

    Province findByUuid(String uuid);

    Page<Province> findByDesignationIlike(String s, Pageable pageable);
}
