package mz.org.fgh.mentoring.service.partner;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.partner.PartnerDTO;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.error.RecordInUseException;
import mz.org.fgh.mentoring.repository.employee.EmployeeRepository;
import mz.org.fgh.mentoring.repository.partner.PartnerRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Singleton
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final EmployeeRepository employeeRepository;

    public PartnerService(PartnerRepository partnerRepository, EmployeeRepository employeeRepository){
        this.partnerRepository = partnerRepository;
        this.employeeRepository = employeeRepository;
    }

    public Partner findPartnerById(@NotNull Long id){
        Optional<Partner> optPartner = partnerRepository.findById(id);
        if(optPartner.isEmpty()){
            throw new MentoringBusinessException("Partner with ID: "+id+" was not found.");
        }
        return optPartner.get();
    }

    public Page findAllPartners(Pageable pageable){
        Page<Partner> partnerList = this.partnerRepository.findAll(pageable);
        return partnerList.map(this::partnerDTO);
    }

    private PartnerDTO partnerDTO(Partner partner){
        return new PartnerDTO(partner);
    }

    public Partner getById(Long id){
       return this.partnerRepository.findById(id).get();

    }

    public @NotNull Partner getMISAU() {
        return partnerRepository.findByUuid("398f0ffeb8fe11edafa10242ac120002")
                .orElseThrow(() -> new MentoringBusinessException("Parceiro MISAU não encontrado."));
    }


    public Page<Partner> findAll(@Nullable Pageable pageable) {
        return partnerRepository.findAll(pageable);
    }

    public Page<Partner> searchByName(String name, Pageable pageable) {
        return partnerRepository.findByNameIlike("%" + name + "%", pageable);
    }

    @Transactional
    public Partner create(Partner partner) {
        partner.setUuid(java.util.UUID.randomUUID().toString());
        partner.setCreatedAt(mz.org.fgh.mentoring.util.DateUtils.getCurrentDate());
        partner.setLifeCycleStatus(mz.org.fgh.mentoring.util.LifeCycleStatus.ACTIVE);
        return partnerRepository.save(partner);
    }
    @Transactional
    public Partner update(Partner partner) {
        Partner existing = partnerRepository.findByUuid(partner.getUuid())
                .orElseThrow(() -> new MentoringBusinessException("Parceiro com UUID " + partner.getUuid() + " não encontrado."));

        existing.setName(partner.getName());
        existing.setDescription(partner.getDescription());
        existing.setUpdatedBy(partner.getUpdatedBy());
        existing.setUpdatedAt(mz.org.fgh.mentoring.util.DateUtils.getCurrentDate());

        return partnerRepository.update(existing);
    }

    @Transactional
    public Partner updateLifeCycleStatus(String uuid, mz.org.fgh.mentoring.util.LifeCycleStatus status, String userUuid) {
        Partner partner = partnerRepository.findByUuid(uuid)
                .orElseThrow(() -> new MentoringBusinessException("Parceiro com UUID " + uuid + " não encontrado."));

        partner.setLifeCycleStatus(status);
        partner.setUpdatedBy(userUuid);
        partner.setUpdatedAt(mz.org.fgh.mentoring.util.DateUtils.getCurrentDate());

        return partnerRepository.update(partner);
    }

    @Transactional
    public void delete(String uuid) {
        Partner partner = partnerRepository.findByUuid(uuid)
                .orElseThrow(() -> new MentoringBusinessException("Parceiro com UUID " + uuid + " não encontrado."));

        // Exemplo de verificação futura:
         long count = employeeRepository.countByPartner(partner);
         if (count > 0) {
             throw new RecordInUseException("Parceiro está associado a funcionários.");
         }

        partnerRepository.delete(partner);
    }

}
