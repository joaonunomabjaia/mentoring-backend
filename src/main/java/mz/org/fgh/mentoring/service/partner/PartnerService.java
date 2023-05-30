package mz.org.fgh.mentoring.service.partner;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.partner.PartnerRepository;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Singleton
public class PartnerService {

    private final PartnerRepository partnerRepository;

    public PartnerService(PartnerRepository partnerRepository){
        this.partnerRepository = partnerRepository;
    }

    public Partner createPartner(Partner partner) {
        if(StringUtils.isEmpty(partner.getName()) && StringUtils.isEmpty(partner.getDescription())){
            throw new MentoringBusinessException("Fields 'NAME' and 'DESCRIPTION' are required.");
        }
        return partnerRepository.save(partner);
    }

    public Partner findPartnerById(@NotNull Long id){
        Optional<Partner> optPartner = partnerRepository.findById(id);
        if(optPartner.isEmpty()){
            throw new MentoringBusinessException("Partner with ID: "+id+" was not found.");
        }
        return optPartner.get();
    }

    public List<Partner> findAllPartners(){
        return partnerRepository.findAll();
    }

    public Partner updatePartner(final Partner partner){

        return this.partnerRepository.update(partner);
    }
}
