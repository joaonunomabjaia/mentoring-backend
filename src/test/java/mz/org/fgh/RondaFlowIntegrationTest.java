package mz.org.fgh;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaMentee;
import mz.org.fgh.mentoring.entity.ronda.RondaMentor;
import mz.org.fgh.mentoring.entity.ronda.RondaType;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;
import mz.org.fgh.mentoring.entity.tutored.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.enums.EnumFlowHistory;
import mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus;
import mz.org.fgh.mentoring.service.ronda.RondaService;
import mz.org.fgh.mentoring.service.tutored.TutoredService;
import mz.org.fgh.mentoring.service.tutored.FlowHistoryService;
import mz.org.fgh.mentoring.service.tutored.FlowHistoryProgressStatusService;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.partner.PartnerRepository;
import mz.org.fgh.mentoring.repository.professionalcategory.ProfessionalCategoryRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaTypeRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class RondaFlowIntegrationTest {

    @Inject
    TutoredService tutoredService;

    @Inject
    FlowHistoryService flowHistoryService;

    @Inject
    FlowHistoryProgressStatusService flowHistoryProgressStatusService;

    @Inject
    RondaService rondaService;

    @Inject
    UserRepository userRepository;

    @Inject
    HealthFacilityRepository healthFacilityRepository;

    @Inject
    TutorRepository tutorRepository;

    @Inject
    RondaTypeRepository rondaTypeRepository;

    @Inject
    ProfessionalCategoryRepository professionalCategoryRepository;

    @Inject
    PartnerRepository partnerRepository;

    @Test
    void createRondaWithSingleMentee() {
        // 0) Buscar um utilizador de teste (por exemplo, "system")
        User user = userRepository.findByUsername("system")
                .orElseThrow(() -> new RuntimeException("User 'system' not found"));

        Long userId = user.getId();

        // ============================================
        // 1) Criar MENTEE mínimo via TutoredService.create
        // ============================================
        Tutored mentee = buildMinimalTutored();

        // Flow/base inicial: SESSAO_ZERO / AGUARDA_INICIO (não isento)
        FlowHistory flowSessaoZero = flowHistoryService
                .findByCode(EnumFlowHistory.SESSAO_ZERO.getCode())
                .orElseThrow(() -> new RuntimeException("FlowHistory SESSAO_ZERO não encontrado"));

        FlowHistoryProgressStatus statusAguarda = flowHistoryProgressStatusService
                .findByCode(EnumFlowHistoryProgressStatus.AGUARDA_INICIO.getCode())
                .orElseThrow(() -> new RuntimeException("FlowHistoryProgressStatus AGUARDA_INICIO não encontrado"));

        Tutored createdMentee = tutoredService.create(
                mentee,
                flowSessaoZero,
                statusAguarda,
                userId
        );
        assertNotNull(createdMentee.getId(), "Mentee deve ser persistido");

        // ============================================
        // 2) Buscar entidades de apoio (HF, Mentor, Tipo de Ronda)
        // ============================================
        // Aqui assumo que já tens alguns dados de referência na BD (via Liquibase)
        HealthFacility hf = healthFacilityRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhuma HealthFacility encontrada para teste"));

        Tutor mentor = tutorRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhum Tutor encontrado para teste"));

        // Ex.: se tiveres códigos "RONDA_ZERO" e "RONDA_CICLO" na tabela ronda_type
        RondaType rondaType = rondaTypeRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhum RondaType encontrado"));

        // ============================================
        // 3) Montar Ronda + RondaDTO com esse mentee
        // ============================================

        Ronda r = new Ronda();

        r.setUuid(Utilities.generateUUID().toString());
        r.setStartDate(DateUtils.getCurrentDate());
        r.setEndDate(DateUtils.getCurrentDate()); // ajusta conforme tua lógica
        r.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        r.setHealthFacility(hf);
        r.setRondaType(rondaType);

        // MENTOR(ES)
        Set<RondaMentor> rondaMentors = new HashSet<>();
        RondaMentor rm = new RondaMentor();
        rm.setUuid(UUID.randomUUID().toString());
        rm.setRonda(r);           // ligado à ronda (será sobrescrito no service)
        rm.setMentor(mentor);
        rm.setStartDate(new Date());
        rm.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        rondaMentors.add(rm);
        r.setRondaMentors(rondaMentors);

        // MENTEE(S)
        Set<RondaMentee> rondaMentees = new HashSet<>();
        RondaMentee rmt = new RondaMentee();
        rmt.setUuid(UUID.randomUUID().toString());
        rmt.setRonda(r);          // ligado à ronda (será sobrescrito no service)
        rmt.setTutored(createdMentee);
        rmt.setStartDate(new Date());
        rmt.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        rondaMentees.add(rmt);
        r.setRondaMentees(rondaMentees);

        RondaDTO dto = new RondaDTO(r);
        //dto.setRonda(r);

        // ============================================
        // 4) Chamar o service de criação da Ronda
        // ============================================
        RondaDTO createdRondaDTO = rondaService.createRonda(dto, userId);
        assertNotNull(createdRondaDTO);
        assertNotNull(createdRondaDTO.getRonda().getId());
        assertFalse(createdRondaDTO.getRonda().getRondaMentees().isEmpty());

        // Aqui já podes depois:
        // - verificar se o FlowHistory do mentee tem RONDA_CICLO / INICIO
        // - usar menteeFlowHistoryService para validar o estado
    }

    /**
     * Constrói um Tutored com Employee mínimo válido
     * (assumindo que tens Partner e ProfessionalCategory válidos na BD).
     */
    private Tutored buildMinimalTutored() {
        Tutored t = new Tutored();
        t.setZeroEvaluationScore(0.0);

        Employee e = new Employee();
        e.setName("Mentee Teste");
        e.setSurname("Demo");
        e.setNuit(123456789);
        e.setTrainingYear(2020);
        e.setPhoneNumber("840000000");
        e.setEmail("mentee.teste@example.com");

        // pegar uma categoria profissional de referência
        ProfessionalCategory cat = professionalCategoryRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhuma ProfessionalCategory encontrada"));
        e.setProfessionalCategory(cat);

        // pegar um parceiro qualquer
        Partner partner = partnerRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhum Partner encontrado"));
        e.setPartner(partner);

        t.setEmployee(e);

        return t;
    }
}

