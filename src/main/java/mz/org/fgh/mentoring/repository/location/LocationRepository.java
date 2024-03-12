package mz.org.fgh.mentoring.repository.location;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.location.Location;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

    @Query(value = "select * from location l where l.EMPLOYEE_ID = :employeeId", nativeQuery = true)
   Location findLocationByEmployeeId(Long employeeId);
}
