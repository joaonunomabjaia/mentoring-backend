package mz.org.fgh.mentoring.repository.partner;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.program.Program;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerRepository extends CrudRepository<Partner, Long> {

    @Query(value = "SELECT DISTINCT(p) FROM Partner p ",
            countQuery = "SELECT COUNT(DISTINCT(p)) FROM Partner p ")
    Page<Partner> findAll(Pageable pageable);

    @Override
    Optional<Partner> findById(@NotNull Long id);

    Partner findByUuid(String uuid);
}
