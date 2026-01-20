package mz.org.fgh;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;
import mz.org.fgh.mentoring.entity.tutored.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.enums.EnumFlowHistory;
import mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus;
import mz.org.fgh.mentoring.repository.employee.EmployeeRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.tutored.FlowHistoryProgressStatusService;
import mz.org.fgh.mentoring.service.tutored.FlowHistoryService;
import mz.org.fgh.mentoring.service.tutored.MenteeFlowHistoryService;
import mz.org.fgh.mentoring.service.tutored.TutoredService;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
@MicronautTest
class MenteeFlowEngineServiceCreateTest {

    @Inject
    TutoredService tutoredService;

    @Inject
    UserRepository userRepository;

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    FlowHistoryService flowHistoryService;

    @Inject
    FlowHistoryProgressStatusService progressStatusService;

    @Inject
    MenteeFlowHistoryService menteeFlowHistoryService;

    @Test
    void shouldInitializeFlowWhenMenteeIsIsento() {
        // 1) User system
        User user = userRepository.findByUsername("system")
                .orElseThrow(() -> new IllegalStateException("System user not found"));

        // 2) Reaproveitar um Employee existente
        Employee employee = StreamSupport.stream(
                employeeRepository.findAll().spliterator(), false
        ).findFirst().orElseThrow(() -> new IllegalStateException("No Employee found"));

        // 3) Construir o mentee mínimo
        Tutored mentee = new Tutored();
        mentee.setEmployee(employee);
        mentee.setZeroEvaluationScore(null);
        mentee.setLifeCycleStatus(LifeCycleStatus.ACTIVE);

        // 4) Flow e status ISENTO para passar à create(...)
        FlowHistory fhZero = flowHistoryService
                .findByCode(EnumFlowHistory.SESSAO_ZERO.getCode())
                .orElseThrow();

        FlowHistoryProgressStatus stIsento = progressStatusService
                .findByCode(EnumFlowHistoryProgressStatus.ISENTO.getCode())
                .orElseThrow();

        // 5) Criar o mentee – já invoca initializeFlowOnCreate internamente
        Tutored created = tutoredService.create(mentee, fhZero, stIsento, user.getId());

        // 6) Buscar histórico da BD
        List<MenteeFlowHistory> history = menteeFlowHistoryService.findByTutored(created);

        // 7) Asserts
        assertEquals(2, history.size());

        MenteeFlowHistory h1 = history.stream()
                .filter(h -> Integer.valueOf(1).equals(h.getSequenceNumber()))
                .findFirst().orElseThrow();

        MenteeFlowHistory h2 = history.stream()
                .filter(h -> Integer.valueOf(2).equals(h.getSequenceNumber()))
                .findFirst().orElseThrow();

        assertEquals(EnumFlowHistory.SESSAO_ZERO.getCode(), h1.getFlowHistory().getCode());
        assertEquals(EnumFlowHistoryProgressStatus.ISENTO.getCode(), h1.getProgressStatus().getCode());

        assertEquals(EnumFlowHistory.RONDA_CICLO.getCode(), h2.getFlowHistory().getCode());
        assertEquals(EnumFlowHistoryProgressStatus.AGUARDA_INICIO.getCode(), h2.getProgressStatus().getCode());
    }
}
