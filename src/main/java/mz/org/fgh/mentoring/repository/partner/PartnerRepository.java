package mz.org.fgh.mentoring.repository.partner;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.partner.Partner;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerRepository extends CrudRepository<Partner, Long> {

    @Override
    List<Partner> findAll();

    @Override
    Optional<Partner> findById(@NotNull Long id);
}
