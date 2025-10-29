package mz.org.fgh.mentoring.service.tutored;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;
import mz.org.fgh.mentoring.entity.tutored.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.enums.EnumFlowHistory;
import mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus;
import mz.org.fgh.mentoring.repository.district.DistrictRepository;
import mz.org.fgh.mentoring.repository.employee.EmployeeRepository;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.location.LocationRepository;
import mz.org.fgh.mentoring.repository.partner.PartnerRepository;
import mz.org.fgh.mentoring.repository.professionalcategory.ProfessionalCategoryRepository;
import mz.org.fgh.mentoring.repository.province.ProvinceRepository;
import mz.org.fgh.mentoring.repository.session.SessionRepository;
import mz.org.fgh.mentoring.repository.tutored.FlowHistoryRepository;
import mz.org.fgh.mentoring.repository.tutored.TutoredRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.employee.EmployeeService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class TutoredService {

    private final EmployeeService employeeService;
    private final TutoredRepository tutoredRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final LocationRepository locationRepository;
    private final DistrictRepository  districtRepository;
    private final ProvinceRepository provinceRepository;
    private final HealthFacilityRepository healthFacilityRepository;
    private final PartnerRepository  partnerRepository;
    private final ProfessionalCategoryRepository  professionalCategoryRepository;
    private final FlowHistoryRepository flowHistoryRepository;

    @Inject
    private SessionRepository sessionRepository;

    @Inject
    private MenteeFlowHistoryService  menteeFlowHistoryService;

    @Inject
    FlowHistoryProgressStatusService flowHistoryProgressStatusService;

    @Inject
    FlowHistoryService flowHistoryService;

    public TutoredService(EmployeeService employeeService, TutoredRepository tutoredRepository, UserRepository userRepository, EmployeeRepository employeeRepository, LocationRepository locationRepository, DistrictRepository districtRepository, ProvinceRepository provinceRepository, HealthFacilityRepository healthFacilityRepository, PartnerRepository partnerRepository, ProfessionalCategoryRepository professionalCategoryRepository, FlowHistoryRepository flowHistoryRepository) {
        this.employeeService = employeeService;
        this.tutoredRepository = tutoredRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.locationRepository = locationRepository;
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
        this.healthFacilityRepository = healthFacilityRepository;
        this.partnerRepository = partnerRepository;
        this.professionalCategoryRepository = professionalCategoryRepository;
        this.flowHistoryRepository = flowHistoryRepository;
    }

    public List<TutoredDTO> findAll(long offset, long  limit){
        List<Tutored> tutoreds = new ArrayList<>();
        List<TutoredDTO> tutoredDTOS = new ArrayList<>();

        if(limit > 0){
            tutoreds = this.tutoredRepository.findTutoredWithLimit(limit, offset);
        }else {
            tutoreds = this.tutoredRepository.findAll();
        }

        for (Tutored tutored :tutoreds) {
            tutoredDTOS.add(new TutoredDTO(tutored));
        }
        return tutoredDTOS;
    }

    public Page<Tutored> findAll(Pageable pageable){
        return tutoredRepository.findAll(pageable);
    }

    public List<Tutored> findAll(){
        List<Tutored> tutoreds = this.tutoredRepository.findAll();
        return tutoreds;
    }

    public List<TutoredDTO> findTutorByUserUuid(String tutorUuid){
        List<Tutored> tutoreds = this.tutoredRepository.findTutoredByTutorUuid(tutorUuid);
        List<TutoredDTO> tutoredDTOS = new ArrayList<>(tutoreds.size());

        for (Tutored tutored : tutoreds){
           tutoredDTOS.add(new TutoredDTO(tutored));
        }
        return tutoredDTOS;
    }

    public List<TutoredDTO> findTutoredByUuid(String uuid){
        List<Tutored> tutoreds = this.tutoredRepository.findTutoredByUuid(uuid);
        List<TutoredDTO> tutoredDTOS = new ArrayList<>(tutoreds.size());

        for (Tutored tutored : tutoreds){
            tutoredDTOS.add(new TutoredDTO(tutored));
        }
        return tutoredDTOS;
    }

    public List<TutoredDTO> searchTutored(Long userId ,Long nuit, String name, String phoneNumber) {

        User user = this.userRepository.findById(userId).get();
        List<Tutored> tutoreds = this.tutoredRepository.search(nuit, name,user, phoneNumber);
        List<TutoredDTO> tutoredDTOS = new ArrayList<>(tutoreds.size());

        for (Tutored tutored : tutoreds){
            tutoredDTOS.add(new TutoredDTO(tutored));
        }
        return tutoredDTOS;
    }

    public TutoredDTO getById(Long id){

        Tutored tutored = this.tutoredRepository.findById(id).get();

        return new TutoredDTO(tutored);
    }

    @Transactional
    public Tutored update(Tutored tutored){

        Optional<Tutored> existing = tutoredRepository.findByUuid(tutored.getUuid());
        if (existing.isEmpty()) throw new RuntimeException("Mentee not found");

        Tutored toUpdate = existing.get();
        toUpdate.getEmployee().setUpdatedBy(tutored.getUpdatedBy());
        toUpdate.getEmployee().setUpdatedAt(tutored.getUpdatedAt());
        toUpdate.getEmployee().setName(tutored.getEmployee().getName());
        toUpdate.getEmployee().setPhoneNumber(tutored.getEmployee().getPhoneNumber());
        toUpdate.getEmployee().setSurname(tutored.getEmployee().getSurname());
        toUpdate.getEmployee().setEmail(tutored.getEmployee().getEmail());
        toUpdate.getEmployee().setNuit(tutored.getEmployee().getNuit());
        toUpdate.getEmployee().setPartner(partnerRepository.findByUuid(tutored.getEmployee().getPartner().getUuid()).get());
        toUpdate.getEmployee().setProfessionalCategory(professionalCategoryRepository.findByUuid(tutored.getEmployee().getProfessionalCategory().getUuid()).get());

        toUpdate.getEmployee().setTrainingYear(tutored.getEmployee().getTrainingYear());
        toUpdate.setUpdatedAt(DateUtils.getCurrentDate());
        toUpdate.setUpdatedBy(tutored.getUpdatedBy());

        locationRepository.deleteAll(toUpdate.getEmployee().getLocations());

        toUpdate.getEmployee().setLocations(tutored.getEmployee().getLocations());

        for (Location location : toUpdate.getEmployee().getLocations()){
            location.setCreatedAt(DateUtils.getCurrentDate());
            location.setCreatedBy(toUpdate.getCreatedBy());
            location.setEmployee(toUpdate.getEmployee());
            location.setUuid(UUID.randomUUID().toString());
            location.setDistrict(districtRepository.findByUuid(location.getDistrict().getUuid()));
            location.setProvince(provinceRepository.findByUuid(location.getProvince().getUuid()));
            location.setHealthFacility(healthFacilityRepository.findByUuid(location.getHealthFacility().getUuid()).get());
        }
        locationRepository.saveAll(toUpdate.getEmployee().getLocations());

        toUpdate.getEmployee().setUpdatedAt(DateUtils.getCurrentDate());
        toUpdate.getEmployee().setUpdatedBy(tutored.getUpdatedBy());
        employeeRepository.update(toUpdate.getEmployee());

        return tutoredRepository.update(toUpdate);
    }

    public TutoredDTO updateTutored(TutoredDTO tutoredDTO, Long userId){
        User user = this.userRepository.findById(userId).get();
        Tutored tutored = new Tutored(tutoredDTO);
        Optional<Tutored> t = tutoredRepository.findByUuid(tutored.getUuid());
        if(t.isPresent()){
            tutored.setCreatedAt(t.get().getCreatedAt());
            tutored.setCreatedBy(t.get().getCreatedBy());
            tutored.setLifeCycleStatus(t.get().getLifeCycleStatus());
            tutored.setId(t.get().getId());
        }

        employeeService.createOrUpdate(tutored.getEmployee(), user);

        tutored.setUpdatedAt(DateUtils.getCurrentDate());
        tutored.setUpdatedBy(user.getUuid());
        this.tutoredRepository.update(tutored);

        return tutoredDTO;
    }

    public TutoredDTO update(TutoredDTO tutoredDTO, Long userId){
        User user = this.userRepository.findById(userId).get();
        Tutored tutored = new Tutored(tutoredDTO);
        Optional<Tutored> t = tutoredRepository.findByUuid(tutored.getUuid());
        if(t.isPresent()){
            tutored.setCreatedAt(t.get().getCreatedAt());
            tutored.setCreatedBy(t.get().getCreatedBy());
            tutored.setLifeCycleStatus(t.get().getLifeCycleStatus());
            tutored.setId(t.get().getId());
            tutored.setEmployee(employeeService.getByUuid(tutored.getEmployee().getUuid()));
        }

        tutored.setUpdatedAt(DateUtils.getCurrentDate());
        tutored.setUpdatedBy(user.getUuid());
        this.tutoredRepository.update(tutored);

        return tutoredDTO;
    }

    public List<Tutored> getTutoredsByHealthFacilityUuids(final List<String> uuids, Long offset, Long limit) {
        if (offset > 0) offset = offset / limit;

        Pageable pageable = Pageable.from(Math.toIntExact(offset), Math.toIntExact(limit));
        return tutoredRepository.getTutoredsByHealthFacilityUuids(uuids, pageable);
    }

    public Page<Tutored> getTutoredsByHealthFacilityUuids(final List<String> uuids, Pageable pageable) {
        return tutoredRepository.findAllOfHealthFacilities(uuids, pageable);
    }

    private boolean checkZeroEvaluation(Tutored tutored) {
        return tutored.getZeroEvaluationScore() != null;
    }

    @Transactional
    public Tutored create(Tutored tutored,
                          FlowHistory flowHistory,
                          FlowHistoryProgressStatus status,
                          Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + userId));

        tutored.setCreatedBy(user.getUuid());
        tutored.setCreatedAt(DateUtils.getCurrentDate());
        tutored.setLifeCycleStatus(LifeCycleStatus.ACTIVE);

        employeeService.createOrUpdate(tutored.getEmployee(), user);

        Tutored newTutored = tutoredRepository.save(tutored);

        boolean isIsento = status.getCode().equals(EnumFlowHistoryProgressStatus.ISENTO.getCode());

        if (isIsento) {
            // Sessão Zero - ISENTO
            createMenteeFlowHistory(newTutored, flowHistory, status, user);

            // RONDA - AGUARDA_INICIO
            FlowHistory ronda = flowHistoryService.findByCode(EnumFlowHistory.RONDA_CICLO.getCode())
                    .orElseThrow(() -> new RuntimeException("FlowHistory não encontrado: RONDA_CICLO"));

            FlowHistoryProgressStatus aguarda = flowHistoryProgressStatusService.findByCode(
                            EnumFlowHistoryProgressStatus.AGUARDA_INICIO.getCode())
                    .orElseThrow(() -> new RuntimeException("FlowHistoryProgressStatus não encontrado: AGUARDA_INICIO"));

            MenteeFlowHistory lastSaved = createMenteeFlowHistory(newTutored, ronda, aguarda, user);
            newTutored.addFlowHistory(lastSaved);

        } else {
            // NÃO ISENTO → SESSAO_ZERO + AGUARDA_INICIO
            FlowHistory sessaoZero = flowHistoryService.findByCode(EnumFlowHistory.SESSAO_ZERO.getCode())
                    .orElseThrow(() -> new RuntimeException("FlowHistory não encontrado: SESSAO_ZERO"));

            FlowHistoryProgressStatus aguarda = flowHistoryProgressStatusService.findByCode(
                            EnumFlowHistoryProgressStatus.AGUARDA_INICIO.getCode())
                    .orElseThrow(() -> new RuntimeException("FlowHistoryProgressStatus não encontrado: AGUARDA_INICIO"));

            MenteeFlowHistory lastSaved = createMenteeFlowHistory(newTutored, sessaoZero, aguarda, user);
            newTutored.addFlowHistory(lastSaved);
        }

        return newTutored;
    }


    /**
     * Método auxiliar para criar e salvar um MenteeFlowHistory.
     */
    private MenteeFlowHistory createMenteeFlowHistory(
            Tutored tutored,
            FlowHistory flowHistory,
            FlowHistoryProgressStatus progressStatus,
            User user
    ) {
        MenteeFlowHistory menteeFlowHistory = new MenteeFlowHistory();
        menteeFlowHistory.setTutored(tutored);
        menteeFlowHistory.setFlowHistory(flowHistory);
        menteeFlowHistory.setProgressStatus(progressStatus);
        menteeFlowHistory.setClassification(0.0);

        return menteeFlowHistoryService.save(menteeFlowHistory, user);
    }

    public Tutored findByUuid(String uuid) {
        return tutoredRepository.findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("Tutored not found"));
    }

    public Optional<Tutored> findOptionalByUuid(String uuid) {
        return tutoredRepository.findByUuid(uuid);
    }

}
