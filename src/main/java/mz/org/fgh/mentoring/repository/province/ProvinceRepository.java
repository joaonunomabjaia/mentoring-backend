package mz.org.fgh.mentoring.repository.province;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.location.Province;

import java.util.List;

@Repository
public interface ProvinceRepository extends CrudRepository<Province, Long> {

    List<Province> findAll();
}
