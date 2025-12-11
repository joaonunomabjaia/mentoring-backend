package mz.org.fgh.mentoring.service.ronda;

import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.base.BaseService;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.dto.ronda.RondaReportDTO;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaMentee;
import mz.org.fgh.mentoring.entity.ronda.RondaMentor;
import mz.org.fgh.mentoring.entity.ronda.RondaType;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.error.NotMatchingProgrammaticArea;
import mz.org.fgh.mentoring.report.RondaSummary;
import mz.org.fgh.mentoring.report.SessionSummary;
import mz.org.fgh.mentoring.repository.answer.AnswerRepository;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaMenteeRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaMentorRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaTypeRepository;
import mz.org.fgh.mentoring.repository.session.SessionRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.tutored.TutoredRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.mentorship.MentorshipService;
import mz.org.fgh.mentoring.service.session.SessionService;
import mz.org.fgh.mentoring.service.tutored.MenteeFlowEngineService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class RondaService extends BaseService {

    private final RondaRepository rondaRepository;
    private final RondaMentorRepository rondaMentorRepository;
    private final RondaMenteeRepository rondaMenteeRepository;
    private final UserRepository userRepository;
    private final TutoredRepository tutoredRepository;
    private final TutorRepository tutorRepository;
    private final HealthFacilityRepository healthFacilityRepository;
    private final RondaTypeRepository rondaTypeRepository;
    private final SessionRepository sessionRepository;
    private final AnswerRepository answerRepository;

    @Inject
    MentorshipService mentorshipService;

    // Novo: motor central de fluxo dos mentees
    @Inject
    private MenteeFlowEngineService menteeFlowEngineService;

    public RondaService(RondaRepository rondaRepository,
                        RondaMentorRepository rondaMentorRepository,
                        RondaMenteeRepository rondaMenteeRepository,
                        UserRepository userRepository,
                        TutoredRepository tutoredRepository,
                        TutorRepository tutorRepository,
                        HealthFacilityRepository healthFacilityRepository,
                        RondaTypeRepository rondaTypeRepository,
                        SessionRepository sessionRepository,
                        AnswerRepository answerRepository) {

        this.rondaRepository = rondaRepository;
        this.rondaMentorRepository = rondaMentorRepository;
        this.rondaMenteeRepository = rondaMenteeRepository;
        this.userRepository = userRepository;
        this.tutoredRepository = tutoredRepository;
        this.tutorRepository = tutorRepository;
        this.healthFacilityRepository = healthFacilityRepository;
        this.rondaTypeRepository = rondaTypeRepository;
        this.sessionRepository = sessionRepository;
        this.answerRepository = answerRepository;
    }

    public List<Ronda> findAll() {
        return this.rondaRepository.findAll();
    }

    public Optional<Ronda> findById(final Long id) {
        return this.rondaRepository.findById(id);
    }

    public Optional<Ronda> findByUuid(final String uuid) {
        return this.rondaRepository.findByUuid(uuid);
    }

    public List<RondaDTO> getAllRondasOfMentor(Long mentorId) {
        List<Ronda> rondaList = this.rondaRepository.getAllRondasOfMentor(mentorId, LifeCycleStatus.BLOCKED);
        List<RondaDTO> rondas = new ArrayList<>();
        for (Ronda ronda : rondaList) {
            RondaDTO rondaDTO = new RondaDTO(ronda);
            rondas.add(rondaDTO);
        }
        return rondas;
    }

    @Transactional
    public List<Ronda> search(@Nullable Long provinceId,
                              @Nullable Long districtId,
                              @Nullable Long healthFacilityId,
                              @Nullable Long mentorId,
                              @Nullable String startDate,
                              @Nullable String endDate) {

        List<Ronda> rondas = rondaRepository.search(provinceId,
                                                    districtId,
                                                    healthFacilityId,
                                                    mentorId,
                                                    DateUtils.createDate(startDate, DateUtils.DDMM_DATE_FORMAT),
                                                    DateUtils.createDate(endDate, DateUtils.DDMM_DATE_FORMAT));

        for (Ronda ronda : rondas) {
            ronda.setRondaMentors(rondaMentorRepository.findByRonda(ronda.getId()));
            ronda.setRondaMentees(rondaMenteeRepository.findByRonda(ronda.getId()));
        }
        return rondas;
    }

    @Transactional
    public List<Ronda> search(@Nullable Long provinceId,
                              @Nullable Long districtId,
                              @Nullable Long healthFacilityId,
                              @Nullable Long mentorId,
                              @Nullable Date startDate,
                              @Nullable Date endDate) {

        return rondaRepository.search(provinceId, districtId, healthFacilityId, mentorId, startDate, endDate);
    }

    // ----------------- CREATE RONDA -----------------

    @Transactional
    public RondaDTO createRonda(RondaDTO rondaDTO, Long userId) {
        Ronda ronda = rondaDTO.getRonda();
        User user = userRepository.findById(userId).get();
        HealthFacility healthFacility = healthFacilityRepository.findByUuid(ronda.getHealthFacility().getUuid()).get();
        Tutor mentor = tutorRepository.findByUuid(ronda.getActiveMentor().getUuid()).get();
        RondaType rondaType = rondaTypeRepository.findByUuid(ronda.getRondaType().getUuid()).get();

        ronda.setCreatedBy(user.getUuid());
        ronda.setCreatedAt(DateUtils.getCurrentDate());
        ronda.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        ronda.setHealthFacility(healthFacility);
        ronda.setRondaType(rondaType);

        Ronda createdRonda = this.rondaRepository.save(ronda);

        // Mentores
        if (Utilities.hasElements(rondaDTO.getRondaMentors())) {
            Set<RondaMentor> rondaMentors = ronda.getRondaMentors();
            Set<RondaMentor> savedRondaMentors = new HashSet<>();
            for (RondaMentor rondaMentor : rondaMentors) {
                rondaMentor.setRonda(createdRonda);
                rondaMentor.setMentor(mentor);
                rondaMentor.setCreatedBy(user.getUuid());
                rondaMentor.setCreatedAt(DateUtils.getCurrentDate());
                rondaMentor.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                RondaMentor saveRondaMentor = this.rondaMentorRepository.save(rondaMentor);
                savedRondaMentors.add(saveRondaMentor);
            }
            createdRonda.setRondaMentors(savedRondaMentors);
        }

        // Mentorandos
        if (Utilities.hasElements(rondaDTO.getRondaMentees())) {
            Set<RondaMentee> rondaMentees = ronda.getRondaMentees();
            Set<RondaMentee> savedRondaMentees = new HashSet<>();
            for (RondaMentee rondaMentee : rondaMentees) {
                rondaMentee.setRonda(createdRonda);
                Tutored mentee = tutoredRepository.findByUuid(rondaMentee.getTutored().getUuid()).get();
                rondaMentee.setTutored(mentee);
                rondaMentee.setCreatedBy(user.getUuid());
                rondaMentee.setCreatedAt(DateUtils.getCurrentDate());
                rondaMentee.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                RondaMentee savedRondaMentee = this.rondaMenteeRepository.save(rondaMentee);
                savedRondaMentees.add(savedRondaMentee);
            }
            createdRonda.setRondaMentees(savedRondaMentees);
        }

        // 🔁 Para cada mentee da ronda → motor de fluxo registra RONDA_CICLO / INICIO
        for (RondaMentee rondaMentee : createdRonda.getRondaMentees()) {
            menteeFlowEngineService.onRoundStarted(
                    rondaMentee.getTutored(),
                    createdRonda,
                    user
            );
        }

        return new RondaDTO(createdRonda);
    }

    // ----------------- OUTROS MÉTODOS -----------------

    public boolean doesUserHaveRondas(User user) {
        List<Ronda> rondas = this.rondaRepository.getByUserUuid(user.getUuid());
        return !rondas.isEmpty();
    }

    public RondaDTO changeMentor(Long rondaId, Long newMentorId, User user) {
        Ronda ronda = rondaRepository.findById(rondaId)
                .orElseThrow(() -> new RuntimeException("Ronda não encontrada"));

        Tutor mentor = tutorRepository.findByIdDetailed(newMentorId)
                .orElseThrow(() -> new RuntimeException("Mentor não encontrado"));

        // Áreas programáticas da Ronda
        Set<ProgrammaticArea> rondaAreas = sessionRepository.findAllOfRonda(ronda.getId()).stream()
                .map(Session::getForm)
                .filter(Objects::nonNull)
                .map(Form::getProgrammaticArea)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Áreas programáticas do novo Mentor
        Set<ProgrammaticArea> mentorAreas = tutorRepository.findByIdDetailed(mentor.getId()).get()
                .getTutorProgrammaticAreas().stream()
                .map(TutorProgrammaticArea::getProgrammaticArea)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Validar compatibilidade
        for (ProgrammaticArea rondaArea : rondaAreas) {
            if (!mentorAreas.contains(rondaArea)) {
                throw new NotMatchingProgrammaticArea(
                        String.format("O mentor selecionado não possui a área programática: %s",
                                rondaArea.getName())
                );
            }
        }

        // Finalizar vínculo anterior ativo
        Set<RondaMentor> existingMentors = rondaMentorRepository.findByRonda(rondaId);
        for (RondaMentor existing : existingMentors) {
            if (existing.getEndDate() == null) {
                existing.setEndDate(new Date());
                rondaMentorRepository.update(existing);
            }
        }

        // Criar novo vínculo
        RondaMentor newRondaMentor = new RondaMentor();
        newRondaMentor.setCreatedAt(new Date());
        newRondaMentor.setStartDate(new Date());
        newRondaMentor.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        newRondaMentor.setCreatedBy(user.getUuid());
        newRondaMentor.setUuid(Utilities.generateUUID().toString());
        newRondaMentor.setMentor(mentor);
        newRondaMentor.setRonda(ronda);
        rondaMentorRepository.save(newRondaMentor);

        ronda.setRondaMentors(rondaMentorRepository.findByRonda(rondaId));
        ronda.setRondaMentees(rondaMenteeRepository.findByRonda(rondaId));

        return new RondaDTO(ronda);
    }

    @Transactional
    public Ronda update(Ronda ronda, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + userId));

        Ronda existingRonda = this.rondaRepository.findByUuid(ronda.getUuid())
                .orElseThrow(() -> new RuntimeException("Ronda não encontrada: " + ronda.getUuid()));

        // 1️⃣ Captura mentees atuais da ronda (antes da atualização)
        Set<RondaMentee> oldMentees = rondaMenteeRepository.findByRonda(existingRonda.getId());

        Set<String> oldMenteeUuids = oldMentees.stream()
                .map(rm -> rm.getTutored().getUuid())
                .collect(Collectors.toSet());

        // 2️⃣ Captura mentees que virão na ronda atualizada (da requisição)
        Set<String> newMenteeUuids = new HashSet<>();
        if (Utilities.hasElements(ronda.getRondaMentees())) {
            for (RondaMentee rm : ronda.getRondaMentees()) {
                if (rm.getTutored() != null && rm.getTutored().getUuid() != null) {
                    newMenteeUuids.add(rm.getTutored().getUuid());
                }
            }
        }

        // 3️⃣ Determina adicionados e removidos
        Set<String> removedMenteeUuids = new HashSet<>(oldMenteeUuids);
        removedMenteeUuids.removeAll(newMenteeUuids); // estavam antes, não estão mais

        Set<String> addedMenteeUuids = new HashSet<>(newMenteeUuids);
        addedMenteeUuids.removeAll(oldMenteeUuids);   // não estavam antes, agora estão

        // 4️⃣ Atualiza campos base da ronda
        ronda.setId(existingRonda.getId());
        ronda.setCreatedAt(existingRonda.getCreatedAt());
        ronda.setCreatedBy(existingRonda.getCreatedBy());
        ronda.setLifeCycleStatus(existingRonda.getLifeCycleStatus());
        ronda.setUpdatedBy(user.getUuid());
        ronda.setUpdatedAt(DateUtils.getCurrentDate());
        ronda.setHealthFacility(
                healthFacilityRepository.findByUuid(ronda.getHealthFacility().getUuid())
                        .orElseThrow(() -> new RuntimeException("Unidade sanitária não encontrada"))
        );
        ronda.setRondaType(
                rondaTypeRepository.findByUuid(ronda.getRondaType().getUuid())
                        .orElseThrow(() -> new RuntimeException("Tipo de Ronda não encontrado"))
        );

        Ronda updatedRonda = this.rondaRepository.update(ronda);

        // 5️⃣ Atualizar relação de mentees na base
        this.rondaMenteeRepository.deleteByRonda(updatedRonda);
        if (Utilities.hasElements(ronda.getRondaMentees())) {
            for (RondaMentee rondaMentee : ronda.getRondaMentees()) {
                rondaMentee.setRonda(updatedRonda);
                rondaMentee.setTutored(
                        tutoredRepository.findByUuid(rondaMentee.getTutored().getUuid())
                                .orElseThrow(() -> new RuntimeException("Mentee não encontrado"))
                );
                rondaMentee.setCreatedBy(user.getUuid());
                rondaMentee.setCreatedAt(DateUtils.getCurrentDate());
                rondaMentee.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                this.rondaMenteeRepository.save(rondaMentee);
            }
        }

        // 6️⃣ Aplicar regras de fluxo:
        //    - para mentees ADICIONADOS → RONDA_CICLO / INICIO
        for (String menteeUuid : addedMenteeUuids) {
            Tutored mentee = tutoredRepository.findByUuid(menteeUuid)
                    .orElseThrow(() -> new RuntimeException("Mentee não encontrado: " + menteeUuid));

            menteeFlowEngineService.onRoundStarted(
                    mentee,
                    updatedRonda,
                    user
            );
        }

        //    - para mentees REMOVIDOS → voltar para RONDA_CICLO / AGUARDA_INICIO
        for (String menteeUuid : removedMenteeUuids) {
            Tutored mentee = tutoredRepository.findByUuid(menteeUuid)
                    .orElseThrow(() -> new RuntimeException("Mentee não encontrado: " + menteeUuid));

            menteeFlowEngineService.onRoundMenteeRemoved(
                    mentee,
                    updatedRonda,
                    user
            );
        }

        // 7️⃣ Recarregar associações
        updatedRonda.setRondaMentees(rondaMenteeRepository.findByRonda(updatedRonda.getId()));
        updatedRonda.setRondaMentors(rondaMentorRepository.findByRonda(updatedRonda.getId()));

        return updatedRonda;
    }


    public Optional<Ronda> getByUuid(String uuid) {
        return this.rondaRepository.findByUuid(uuid);
    }

    @Transactional
    public void delete(String uuid, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + userId));

        Ronda ronda = this.rondaRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Ronda não encontrada: " + uuid));

        this.rondaMenteeRepository.deleteByRonda(ronda);
        this.rondaMentorRepository.deleteByRonda(ronda);

        // agora com user
        menteeFlowEngineService.onRondaDeleted(ronda, user);

        this.rondaRepository.delete(ronda);
    }

    // ----------------- RELATÓRIO -----------------

    public RondaReportDTO generateReport(String uuid) {
        Optional<Ronda> existingRonda = this.rondaRepository.findByUuid(uuid);
        RondaReportDTO rod = new RondaReportDTO();

        Ronda ronda = null;
        if (existingRonda.isPresent()) {
            ronda = existingRonda.get();
            loadRondaDependencies(ronda);
        }

        List<RondaSummary> rondaSummaryList = new ArrayList<>();

        for (RondaMentee mentee : ronda.getRondaMentees()) {
            RondaSummary rondaSummary = new RondaSummary();
            rondaSummary.setZeroEvaluation(
                    Utilities.roundToOneDecimalPlace(mentee.getTutored().getZeroEvaluationScore())
                            .doubleValue()
            );
            rondaSummary.setMentor(ronda.getActiveMentor().getEmployee().getFullName());
            rondaSummary.setMentee(mentee.getTutored().getEmployee().getFullName());
            rondaSummary.setNuit(mentee.getTutored().getEmployee().getNuit());

            List<Session> sessions = new ArrayList<>();
            for (Session session : ronda.getSessions()) {
                if (session.getMentee().equals(mentee.getTutored())) {
                    sessions.add(session);
                }
            }
            sessions.sort(Comparator.comparing(Session::getStartDate));

            rondaSummary.setSummaryDetails(new HashMap<>());
            int i = 1;
            for (Session session : sessions) {
                rondaSummary.getSummaryDetails().put(i, generateSessionSummary(session));
                i++;
            }

            rondaSummary.setSession1(Utilities.roundToOneDecimalPlace(
                    determineSessionScore(rondaSummary.getSummaryDetails().get(1))).doubleValue());
            rondaSummary.setSession2(Utilities.roundToOneDecimalPlace(
                    determineSessionScore(rondaSummary.getSummaryDetails().get(2))).doubleValue());
            rondaSummary.setSession3(Utilities.roundToOneDecimalPlace(
                    determineSessionScore(rondaSummary.getSummaryDetails().get(3))).doubleValue());
            rondaSummary.setSession4(Utilities.roundToOneDecimalPlace(
                    determineSessionScore(rondaSummary.getSummaryDetails().get(4))).doubleValue());
            rondaSummary.setFinalScore(
                    rondaSummary.getSession4() < 86 ? "Repetir Ronda" : "Graduado"
            );

            Map<String, List<String>> summaryDetails = new HashMap<>();

            for (Map.Entry<Integer, List<SessionSummary>> entry :
                    rondaSummary.getSummaryDetails().entrySet()) {
                for (SessionSummary summary : entry.getValue()) {
                    if (!summaryDetails.containsKey(String.valueOf(summary.getTitle()))) {
                        summaryDetails.put(String.valueOf(summary.getTitle()), new ArrayList<>());
                        summaryDetails.get(summary.getTitle()).add(summary.getTitle());
                    }
                    summaryDetails.get(summary.getTitle()).add(
                            Utilities.roundToOneDecimalPlace(summary.getProgressPercentage())
                                    .toString() + "%"
                    );
                }
            }
            rondaSummary.setSummaryDetailsMap(summaryDetails);

            rondaSummaryList.add(rondaSummary);
        }

        rod.setRondaSummaryList(rondaSummaryList);
        return rod;
    }

    private List<SessionSummary> generateSessionSummary(Session session) {
        List<SessionSummary> summaries = new ArrayList<>();

        for (Mentorship mentorship : session.getMentorships()) {
            if (mentorship.isPatientEvaluation()) {
                for (Answer answer : mentorship.getAnswers()) {
                    String cat = answer.getFormSectionQuestion()
                            .getFormSection().getSection().getDescription();
                    if (categoryAlreadyExists(cat, summaries)) {
                        doCountInCategory(cat, summaries, answer);
                    } else {
                        summaries.add(initSessionSummary(answer));
                    }
                }
                break;
            }
        }
        return summaries;
    }

    private void doCountInCategory(String cat,
                                   List<SessionSummary> summaries,
                                   Answer answer) {

        for (SessionSummary sessionSummary : summaries) {
            if (sessionSummary.getTitle().equals(cat)) {
                if ("SIM".equals(answer.getValue())) {
                    sessionSummary.setSimCount(sessionSummary.getSimCount() + 1);
                } else if ("NAO".equals(answer.getValue())) {
                    sessionSummary.setNaoCount(sessionSummary.getNaoCount() + 1);
                }
            }
        }
    }

    private boolean categoryAlreadyExists(String cat, List<SessionSummary> summaries) {
        for (SessionSummary sessionSummary : summaries) {
            if (Utilities.stringHasValue(sessionSummary.getTitle())
                    && sessionSummary.getTitle().equals(cat)) {
                return true;
            }
        }
        return false;
    }

    private SessionSummary initSessionSummary(Answer answer) {
        SessionSummary sessionSummary = new SessionSummary();
        sessionSummary.setTitle(
                answer.getFormSectionQuestion().getFormSection().getSection().getDescription()
        );

        if ("SIM".equals(answer.getValue())) {
            sessionSummary.setSimCount(sessionSummary.getSimCount() + 1);
        } else if ("NAO".equals(answer.getValue())) {
            sessionSummary.setNaoCount(sessionSummary.getNaoCount() + 1);
        }
        return sessionSummary;
    }

    private void loadRondaDependencies(Ronda ronda) {
        ronda.setRondaMentees(rondaMenteeRepository.findByRonda(ronda.getId()));
        ronda.setRondaMentors(rondaMentorRepository.findByRonda(ronda.getId()));
        ronda.setSessions(sessionRepository.findAllOfRonda(ronda.getId()));
        for (Session session : ronda.getSessions()) {
            session.setMentorships(mentorshipService.findAllOfSession(session));
            for (Mentorship mentorship : session.getMentorships()) {
                mentorship.setAnswers(answerRepository.findByMentorship(mentorship));
            }
        }
    }

    private double determineSessionScore(List<SessionSummary> sessionSummaries) {
        int yesCount = 0;
        int noCount = 0;
        for (SessionSummary sessionSummary : sessionSummaries) {
            yesCount = yesCount + sessionSummary.getSimCount();
            noCount = noCount + sessionSummary.getNaoCount();
            sessionSummary.setProgressPercentage(
                    (double) sessionSummary.getSimCount()
                            / (sessionSummary.getSimCount() + sessionSummary.getNaoCount()) * 100
            );
        }
        return (double) yesCount / (yesCount + noCount) * 100;
    }

    // ----------------- UPDATE MANY -----------------

    @Transactional
    public List<Ronda> updateMany(List<Ronda> rondas, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + userId));

        List<Ronda> updatedRondas = new ArrayList<>();

        for (Ronda ronda : rondas) {
            Ronda existingRonda = rondaRepository.findByUuid(ronda.getUuid())
                    .orElseThrow(() -> new RuntimeException("Ronda não encontrada: " + ronda.getUuid()));

            // Campos auditáveis e relacionamentos
            addUpdateAuditInfo(ronda, existingRonda, user);
            ronda.setId(existingRonda.getId());
            ronda.setHealthFacility(
                    healthFacilityRepository.findByUuid(ronda.getHealthFacility().getUuid())
                            .orElseThrow(() -> new RuntimeException("Unidade sanitária não encontrada"))
            );
            ronda.setRondaType(
                    rondaTypeRepository.findByUuid(ronda.getRondaType().getUuid())
                            .orElseThrow(() -> new RuntimeException("Tipo de Ronda não encontrado"))
            );

            // Atualiza a ronda
            Ronda updated = rondaRepository.update(ronda);
            updatedRondas.add(updated);

            // Carrega mentees, mentores, sessões, mentorships e answers
            loadRondaDependencies(updated);

            // Para cada mentee, calcula a classificação final e escreve o histórico de fecho
            if (ronda.getRondaMentees() != null) {
                for (RondaMentee rondaMentee : ronda.getRondaMentees()) {
                    Tutored mentee = rondaMentee.getTutored();
                    if (mentee == null) continue;

                    MenteeFlowHistory mfh =
                            rondaMentee.getTutored().getMenteeFlowHistories().stream()
                                    .filter(h -> h.getSequenceNumber() != null)
                                    .max(Comparator.comparing(MenteeFlowHistory::getSequenceNumber))
                                    .orElse(null);
                    double classification = (mfh != null) ? mfh.getClassification() : 0.0;
                    // Motor de fluxo: RONDA_CICLO / TERMINADO + próximo estado (RONDA_CICLO ou SESSAO_SEMESTRAL)
                    Tutored savedTutored = tutoredRepository.findByUuid(mentee.getUuid()).orElseThrow(() -> new RuntimeException("Mentorando não encontrado"));

                    menteeFlowEngineService.onRoundFinished(
                            savedTutored,
                            updated,
                            classification,
                            user
                    );
                }
            }
        }

        return updatedRondas;
    }


    // ----------------- MOTOR DE INTERRUPÇÃO (DELEGADO) -----------------

    /**
     * Delegador para o motor central de fluxo:
     *
     * - Procura MenteeFlowHistory em RONDA_CICLO / INICIO
     * - Para cada um, verifica se passaram > 60 dias desde última mentoria
     * - Se sim, marca INTERROMPIDO e cria novo AGUARDA_INICIO.
     */
    @Transactional
    public void processRondaInterruptionEngine(Long userId) {
        User systemUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + userId));

        menteeFlowEngineService.checkAndInterruptInactiveRounds(systemUser);
    }

    public List<Ronda> getAllOfMentors(List<String> mentorUuids) {
        List<Ronda> rondas = rondaRepository.findByMentorUuidIn(mentorUuids);

        if (!rondas.isEmpty()) {
            for (Ronda ronda : rondas) {
                ronda.setRondaMentors(rondaMentorRepository.findMentorsForRondas(ronda.getId()));
                ronda.setRondaMentees(rondaMenteeRepository.findMenteesForRondas(ronda.getId()));
            }

        }
        return rondas;
    }

}
