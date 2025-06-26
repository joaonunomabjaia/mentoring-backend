package mz.org.fgh.mentoring.repository.partner;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.partner.Partner;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Page<Partner> findByNameIlike(String s, Pageable pageable);

    Optional<Partner> findByUuid(String uuid);

}
