package mz.org.fgh.mentoring.service.partner;

import io.micronaut.data.model.Page;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.partner.PartnerDTO;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.partner.PartnerRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.employee.EmployeeService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import org.apache.commons.lang3.StringUtils;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import io.micronaut.data.model.Pageable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final UserRepository userRepository;

    @Inject
    private EmployeeService employeeService;

    public PartnerService(PartnerRepository partnerRepository, UserRepository userRepository){
        this.partnerRepository = partnerRepository;
        this.userRepository = userRepository;
    }

    public Partner createPartner(Partner partner, Long userId) {
        if(StringUtils.isEmpty(partner.getName()) && StringUtils.isEmpty(partner.getDescription())){
            throw new MentoringBusinessException("Fields 'NAME' and 'DESCRIPTION' are required.");
        }
        User user = userRepository.findById(userId).get();
        partner.setCreatedBy(user.getUuid());
        partner.setUuid(UUID.randomUUID().toString());
        partner.setCreatedAt(DateUtils.getCurrentDate());
        partner.setLifeCycleStatus(LifeCycleStatus.ACTIVE);

        return partnerRepository.save(partner);
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

    public Partner updatePartner(final Partner partner, Long userId){
        Partner partnerDB = getById(partner.getId());
        User user = userRepository.findById(userId).get();
        partnerDB.setUpdatedBy(user.getUuid());
        partnerDB.setUpdatedAt(DateUtils.getCurrentDate());
        partnerDB.setName(partner.getName());
        partnerDB.setDescription(partner.getDescription());
        return this.partnerRepository.update(partnerDB);
    }

//    public List<PartnerDTO> getAll() {
//        try {
//            return Utilities.parseList(this.partnerRepository.findAll(), PartnerDTO.class);
//        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    public Partner getById(Long id){
       return this.partnerRepository.findById(id).get();

    }

    @Transactional
    public Partner delete(Partner partner, Long userId) {
        User user = userRepository.findById(userId).get();
        partner.setLifeCycleStatus(LifeCycleStatus.DELETED);
        partner.setUpdatedBy(user.getUuid());
        partner.setUpdatedAt(DateUtils.getCurrentDate());

        return this.partnerRepository.update(partner);
    }

    @Transactional
    public void destroy(Partner partner) {
        List<Employee> partners = employeeService.findEmployeebyProfessionalCategory(partner.getId());
     
        if(partners.isEmpty()) {
            partnerRepository.delete(partner);
        }
    }

    public @NotNull Partner getMISAU() {
        return partnerRepository.findByUuid("398f0ffeb8fe11edafa10242ac120002");
    }
}
