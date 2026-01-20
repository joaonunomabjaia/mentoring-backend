package mz.org.fgh.mentoring.service.employee;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.district.DistrictRepository;
import mz.org.fgh.mentoring.repository.employee.EmployeeRepository;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.location.LocationRepository;
import mz.org.fgh.mentoring.repository.partner.PartnerRepository;
import mz.org.fgh.mentoring.repository.professionalcategory.ProfessionalCategoryRepository;
import mz.org.fgh.mentoring.repository.province.ProvinceRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final ProvinceRepository provinceRepository;

    private final DistrictRepository districtRepository;

    private final HealthFacilityRepository healthFacilityRepository;

    private final ProfessionalCategoryRepository professionalCategoryRepository;

    private final PartnerRepository partnerRepository;

    private final LocationRepository locationRepository;

    public EmployeeService(EmployeeRepository employeeRepository, UserRepository userRepository, ProvinceRepository provinceRepository, DistrictRepository districtRepository, HealthFacilityRepository healthFacilityRepository, ProfessionalCategoryRepository professionalCategoryRepository, PartnerRepository partnerRepository, LocationRepository locationRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.healthFacilityRepository = healthFacilityRepository;
        this.professionalCategoryRepository = professionalCategoryRepository;
        this.partnerRepository = partnerRepository;
        this.locationRepository = locationRepository;
    }

    public Employee getById(Long id){
        return this.employeeRepository.findById(id).get();
    }

    public Employee getByUuid(String uuid){
        return this.employeeRepository.findByUuid(uuid).get();
    }

    private void resolveFKs(Location location) {
        if (location.getProvince() != null)
            location.setProvince(provinceRepository.findByUuid(location.getProvince().getUuid()));

        if (location.getDistrict() != null)
            location.setDistrict(districtRepository.findByUuid(location.getDistrict().getUuid()));

        if (location.getHealthFacility() != null)
            location.setHealthFacility(
                    healthFacilityRepository.findByUuid(location.getHealthFacility().getUuid()).orElseThrow()
            );
    }


    private void applyLocationData(Location from, Location to) {
        resolveFKs(from);

        to.setProvince(from.getProvince());
        to.setDistrict(from.getDistrict());
        to.setHealthFacility(from.getHealthFacility());
        to.setLocationLevel(from.getLocationLevel());
    }

    @Transactional
    public Employee createOrUpdate(Employee incoming, User user) {

        /* =========================================================
         * 1️⃣ Resolver FKs do Employee
         * ========================================================= */
        incoming.setProfessionalCategory(
                professionalCategoryRepository.findByUuid(
                        incoming.getProfessionalCategory().getUuid()
                ).orElseThrow()
        );

        incoming.setPartner(
                partnerRepository.findByUuid(
                        incoming.getPartner().getUuid()
                ).orElseThrow()
        );

        /* =========================================================
         * 2️⃣ Persistir Employee base
         * ========================================================= */
        Employee dbEmployee = employeeRepository.createOrUpdate(incoming, user);

        /* =========================================================
         * 3️⃣ Validação
         * ========================================================= */
        if (incoming.getLocations() == null || incoming.getLocations().isEmpty()) {
            throw new RuntimeException("É obrigatório indicar pelo menos uma localização.");
        }

        /* =========================================================
         * 4️⃣ Buscar locations atuais da BD
         * ========================================================= */
        Set<Location> dbLocations =
                locationRepository.findByEmployeeId(dbEmployee.getId());

        /* =========================================================
         * 5️⃣ Mapear incoming por ID
         * ========================================================= */
        Map<Long, Location> incomingById =
                incoming.getLocations().stream()
                        .filter(l -> l.getId() != null)
                        .collect(Collectors.toMap(
                                Location::getId,
                                l -> l
                        ));

        /* =========================================================
         * 6️⃣ Atualizar / Inativar existentes
         * ========================================================= */
        for (Location dbLoc : dbLocations) {

            Location incomingLoc = incomingById.get(dbLoc.getId());

            if (incomingLoc == null) {
                // ❌ Removida
                if (dbLoc.getLifeCycleStatus() == LifeCycleStatus.ACTIVE) {
                    dbLoc.setLifeCycleStatus(LifeCycleStatus.INACTIVE);
                    dbLoc.setUpdatedBy(user.getUuid());
                    dbLoc.setUpdatedAt(DateUtils.getCurrentDate());
                    locationRepository.update(dbLoc);
                }
            } else {
                // ✔️ Atualizar
                applyLocationData(incomingLoc, dbLoc);
                dbLoc.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                dbLoc.setUpdatedBy(user.getUuid());
                dbLoc.setUpdatedAt(DateUtils.getCurrentDate());
                locationRepository.update(dbLoc);
            }
        }

        /* =========================================================
         * 7️⃣ Criar NOVAS (INSTÂNCIA NOVA!)
         * ========================================================= */
        incoming.getLocations().stream()
                .filter(l -> l.getId() == null)
                .forEach(incomingLoc -> {

                    Location newLoc = new Location();

                    applyLocationData(incomingLoc, newLoc);
                    resolveFKs(newLoc);

                    newLoc.setUuid(Utilities.generateUUID());
                    newLoc.setEmployee(dbEmployee);
                    newLoc.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                    newLoc.setCreatedBy(user.getUuid());
                    newLoc.setCreatedAt(DateUtils.getCurrentDate());

                    locationRepository.save(newLoc);
                });

        return dbEmployee;
    }



    public Optional<Employee> findById(Long id) {
        return this.employeeRepository.findById(id);
    }

    public void update(Employee employeeDB) {
        this.employeeRepository.update(employeeDB);
    }
}
