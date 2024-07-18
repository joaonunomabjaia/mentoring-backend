package mz.org.fgh.mentoring.service.ronda;

import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.base.BaseService;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.dto.ronda.RondaReportDTO;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaMentee;
import mz.org.fgh.mentoring.entity.ronda.RondaMentor;
import mz.org.fgh.mentoring.entity.ronda.RondaType;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;
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
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    public RondaService(RondaRepository rondaRepository, RondaMentorRepository rondaMentorRepository,
                        RondaMenteeRepository rondaMenteeRepository, UserRepository userRepository,
                        TutoredRepository tutoredRepository, TutorRepository tutorRepository,
                        HealthFacilityRepository healthFacilityRepository, RondaTypeRepository rondaTypeRepository, SessionRepository sessionRepository, AnswerRepository answerRepository) {
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

    public List<Ronda> findAll(){
        return this.rondaRepository.findAll();
    }

    public List<Ronda> findAllRondaWithAll(){
        return this.rondaRepository.findAllRondaWithAll();
    }

    public Optional<Ronda> findById(final Long id){
        return this.rondaRepository.findById(id);
    }

    public Optional<Ronda> findByUuid(final String uuid){
      return this.rondaRepository.findByUuid(uuid);
    }

    public List<Ronda> findRondaWithLimit(long limit, long offset){
        return this.rondaRepository.findRondaWithLimit(limit, offset);
    }

    public Ronda createRonda(final Ronda ronda, Long userId){
        User user = userRepository.findById(userId).get();
        ronda.setCreatedBy(user.getUuid());
        ronda.setCreatedAt(DateUtils.getCurrentDate());
        ronda.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        return this.rondaRepository.save(ronda);
    }

    public List<RondaDTO> getAllRondasOfMentor(Long mentorId) {
        List<Ronda> rondaList = this.rondaRepository.getAllRondasOfMentor(mentorId, LifeCycleStatus.BLOCKED);
        List<RondaDTO> rondas = new ArrayList<>();
        for (Ronda ronda: rondaList) {
            RondaDTO rondaDTO = new RondaDTO(ronda);
            rondas.add(rondaDTO);
        }
        return rondas;
    }

    public List<RondaDTO> createRondas(List<RondaDTO> rondaDTOS, Long userId) {
        List<RondaDTO> rondas = new ArrayList<>();
        for (RondaDTO rondaDTO: rondaDTOS) {
            RondaDTO dto = this.createRonda(rondaDTO, userId);
            rondas.add(dto);
        }
      return rondas;
    }

    @Transactional
    public List<Ronda> search(@Nullable Long provinceId, @Nullable Long districtId, @Nullable Long healthFacilityId, @Nullable Long mentorId, @Nullable String startDate, @Nullable String endDate) {
        Date start = convertToDate(startDate);
        Date end = convertToDate(endDate);
        List<Ronda> rondas = rondaRepository.search(provinceId, districtId, healthFacilityId, mentorId, start, end);
        for (Ronda ronda: rondas){
            ronda.setRondaMentors(rondaMentorRepository.findByRonda(ronda.getId()));
            ronda.setRondaMentees(rondaMenteeRepository.findByRonda(ronda.getId()));
        }
        return rondas;
    }

    @Transactional
    public List<Ronda> search(@Nullable Long provinceId,@Nullable Long districtId,@Nullable Long healthFacilityId,@Nullable Long mentorId,@Nullable Date startDate,@Nullable Date endDate) {
        return rondaRepository.search(provinceId, districtId, healthFacilityId, mentorId, startDate, endDate);
    }


    public static Date convertToDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;  // Se houver erro na formatação, retorna null
        }
    }



    @Transactional
    public RondaDTO createRonda(RondaDTO rondaDTO, Long userId) {
        Ronda ronda = rondaDTO.getRonda();
        User user = userRepository.findById(userId).get();
        HealthFacility healthFacility = healthFacilityRepository.findByUuid(ronda.getHealthFacility().getUuid()).get();
        Tutor mentor = tutorRepository.findByUuid(ronda.getRondaMentors().get(0).getMentor().getUuid()).get();
        RondaType rondaType = rondaTypeRepository.findByUuid(ronda.getRondaType().getUuid()).get();
        //if (isOnActiveRonda(mentor)) throw new RuntimeException("O mentor encontra-se em uma ronda activa, não é possível criar nova.");

        ronda.setCreatedBy(user.getUuid());
        ronda.setCreatedAt(DateUtils.getCurrentDate());
        ronda.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        ronda.setHealthFacility(healthFacility);
        ronda.setRondaType(rondaType);
        Ronda createdRonda = this.rondaRepository.save(ronda);
        if(Utilities.listHasElements(rondaDTO.getRondaMentors())) {
            List<RondaMentor> rondaMentors = ronda.getRondaMentors();
            List<RondaMentor> savedRondaMentors = new ArrayList<>();
            for (RondaMentor rondaMentor: rondaMentors) {
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
        if(Utilities.listHasElements(rondaDTO.getRondaMentees())) {
            List<RondaMentee> rondaMentees = ronda.getRondaMentees();
            List<RondaMentee> savedRondaMentees = new ArrayList<>();
            for (RondaMentee rondaMentee: rondaMentees) {
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
        return new RondaDTO(createdRonda);
    }

    private boolean isOnActiveRonda(Tutor mentor) {
        List<Ronda> rondas = getAllOfMentor(mentor.getUuid());
        if (!Utilities.listHasElements(rondas)) { return false;}
        for (Ronda ronda: rondas) {
            if (!ronda.isComplete()) return false;
        }
        return true;
    }

    public List<Ronda> getAllOfMentor(String mentorUuid) {
        return this.rondaRepository.getAllOfMentor(mentorUuid);
    }

    public boolean doesUserHaveRondas(User user) {
        List<Ronda> rondas = this.rondaRepository.getByUserUuid(user.getUuid());
        return !rondas.isEmpty();
    }

    public boolean doesHealthFacilityHaveRondas(HealthFacility healthFacility) {
        List<Ronda> rondas = this.rondaRepository.getByHealthFacilityId(healthFacility.getId());
        return !rondas.isEmpty();
    }

    public RondaDTO changeMentor(Long rondaId, Long newMentorId, User user) {
        Ronda ronda = rondaRepository.findById(rondaId).get();
        Tutor mentor = tutorRepository.findById(newMentorId).get();

        RondaMentor rondaMentor = new RondaMentor();

        rondaMentor.setCreatedAt(new Date());
        rondaMentor.setMentor(mentor);
        rondaMentor.setCreatedBy(user.getUuid());
        rondaMentor.setStartDate(new Date());
        rondaMentor.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        rondaMentor.setUuid(Utilities.generateUUID().toString());
        rondaMentor.setRonda(ronda);

        ronda.setRondaMentors(rondaMentorRepository.findByRonda(ronda.getId()));
        ronda.setRondaMentees(rondaMenteeRepository.findByRonda(ronda.getId()));

        for(RondaMentor rondaMentor1: ronda.getRondaMentors()){
            if(rondaMentor1.getEndDate() == null) {
                rondaMentor1.setEndDate(new Date());
                rondaMentorRepository.update(rondaMentor1);
            }
        }

        rondaMentorRepository.save(rondaMentor);

        return new RondaDTO(ronda);
    }

    @Transactional
    public Ronda update(Ronda ronda, Long userId) {

        User user = userRepository.findById(userId).get();
        //if (isOnActiveRonda(mentor)) throw new RuntimeException("O mentor encontra-se em uma ronda activa, não é possível criar nova.");

        Optional<Ronda> existingRonda = this.rondaRepository.findByUuid(ronda.getUuid());

        ronda.setId(existingRonda.get().getId());
        ronda.setCreatedAt(existingRonda.get().getCreatedAt());
        ronda.setCreatedBy(existingRonda.get().getCreatedBy());
        ronda.setLifeCycleStatus(existingRonda.get().getLifeCycleStatus());
        ronda.setUpdatedBy(user.getUuid());
        ronda.setUpdatedAt(DateUtils.getCurrentDate());
        ronda.setHealthFacility(healthFacilityRepository.findByUuid(ronda.getHealthFacility().getUuid()).get());
        ronda.setRondaType(rondaTypeRepository.findByUuid(ronda.getRondaType().getUuid()).get());
        Ronda createdRonda = this.rondaRepository.update(ronda);

        this.rondaMenteeRepository.deleteByRonda(ronda);

        if(Utilities.listHasElements(ronda.getRondaMentees())) {
            for (RondaMentee rondaMentee: ronda.getRondaMentees()) {
                rondaMentee.setRonda(createdRonda);
                rondaMentee.setTutored(tutoredRepository.findByUuid(rondaMentee.getTutored().getUuid()).get());
                rondaMentee.setCreatedBy(user.getUuid());
                rondaMentee.setCreatedAt(DateUtils.getCurrentDate());
                rondaMentee.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                this.rondaMenteeRepository.save(rondaMentee);
            }
        }
        ronda.setRondaMentees(rondaMenteeRepository.findByRonda(ronda.getId()));
        ronda.setRondaMentors(rondaMentorRepository.findByRonda(ronda.getId()));
        return ronda;
    }

    public Optional<Ronda> getByUuid(String uuid) {
        return this.rondaRepository.findByUuid(uuid);
    }

    @Transactional
    public void delete(String uuid) {
        Optional<Ronda> existingRonda = this.rondaRepository.findByUuid(uuid);
        this.rondaMenteeRepository.deleteByRonda(existingRonda.get());
        this.rondaMentorRepository.deleteByRonda(existingRonda.get());
        this.rondaRepository.delete(existingRonda.get());
    }

    public RondaReportDTO generateReport(String uuid) {
        Optional<Ronda> existingRonda = this.rondaRepository.findByUuid(uuid);
        RondaReportDTO rod = new RondaReportDTO();

        Ronda ronda = null;
        if (existingRonda.isPresent()) {
            ronda = existingRonda.get();
            loadRondaDependencies(ronda);
        }

        List<RondaSummary> rondaSummaryList = new ArrayList<>();


        for (RondaMentee mentee : ronda.getRondaMentees()){
            RondaSummary rondaSummary = new RondaSummary();
            rondaSummary.setRonda(ronda);
            rondaSummary.setZeroEvaluation(mentee.getTutored().getZeroEvaluationScore());
            rondaSummary.setMentor(ronda.getActiveMentor().getEmployee().getFullName());
            rondaSummary.setMentee(mentee.getTutored().getEmployee().getFullName());
            rondaSummary.setNuit(mentee.getTutored().getEmployee().getNuit());
            List<Session> sessions = new ArrayList<>();
            for (Session session : ronda.getSessions()){
                if (session.getMentee().equals(mentee.getTutored())){
                    sessions.add(session);
                }
            }
            sessions.sort(Comparator.comparing(Session::getStartDate));
            rondaSummary.setSummaryDetails(new HashMap<>());
            int i = 1;
            for (Session session : sessions){
                rondaSummary.getSummaryDetails().put(i, generateSessionSummary(session));

                i++;
            }
            rondaSummary.setSession1(determineSessionScore(rondaSummary.getSummaryDetails().get(1)));
            rondaSummary.setSession2(determineSessionScore(rondaSummary.getSummaryDetails().get(2)));
            rondaSummary.setSession3(determineSessionScore(rondaSummary.getSummaryDetails().get(3)));
            rondaSummary.setSession4(determineSessionScore(rondaSummary.getSummaryDetails().get(4)));
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
                    String cat = answer.getQuestion().getQuestionCategory().getCategory();
                    if (categoryAlreadyExists(cat, summaries)){
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

    private void doCountInCategory(String cat, List<SessionSummary> summaries, Answer answer) {
        for (SessionSummary sessionSummary : summaries) {
            if (sessionSummary.getTitle().equals(cat)) {
                if (answer.getValue().equals("SIM")) {
                    sessionSummary.setSimCount(sessionSummary.getSimCount() + 1);
                } else if (answer.getValue().equals("NAO")) {
                    sessionSummary.setNaoCount(sessionSummary.getNaoCount() + 1);
                }
            }
        }
    }
    private boolean categoryAlreadyExists(String cat, List<SessionSummary> summaries) {
        for (SessionSummary sessionSummary : summaries) {
            if (sessionSummary.getTitle().equals(cat)) {
                return true;
            }
        }
        return false;
    }

    private SessionSummary initSessionSummary(Answer answer) {
        SessionSummary sessionSummary = new SessionSummary();
        sessionSummary.setTitle(answer.getQuestion().getQuestionCategory().getCategory());

        if (answer.getValue().equals("SIM")) {
            sessionSummary.setSimCount(sessionSummary.getSimCount() + 1);
        } else if (answer.getValue().equals("NAO")) {
            sessionSummary.setNaoCount(sessionSummary.getNaoCount() + 1);
        }
        return sessionSummary;
    }

    private void loadRondaDependencies(Ronda ronda) {
        ronda.setRondaMentees(rondaMenteeRepository.findByRonda(ronda.getId()));
        ronda.setRondaMentors(rondaMentorRepository.findByRonda(ronda.getId()));
        ronda.setSessions(sessionRepository.findAllOfRonda(ronda.getId()));
        for (Session session : ronda.getSessions()){
            for (Mentorship mentorship : session.getMentorships()){
                mentorship.setAnswers(answerRepository.findByMentorship(mentorship));
            }
        }
    }

    private double determineSessionScore(List<SessionSummary> sessionSummaries) {
        int yesCount = 0;
        int noCount = 0;
        for (SessionSummary sessionSummary : sessionSummaries){
            yesCount = yesCount + sessionSummary.getSimCount();
            noCount = noCount + sessionSummary.getNaoCount();
        }
        return (double) yesCount / (yesCount + noCount) *100;
    }

    @Transactional
    public List<Ronda> updateMany(List<Ronda> rondas, Long userId) {
        List<Ronda> updatedRondas = new ArrayList<>();
        for (Ronda ronda : rondas) {
            User user = userRepository.findById(userId).get();

            Optional<Ronda> existingRonda = this.rondaRepository.findByUuid(ronda.getUuid());

            ronda.setId(existingRonda.get().getId());
            addUpdateAuditInfo(ronda, existingRonda.get(), user);
            ronda.setHealthFacility(healthFacilityRepository.findByUuid(ronda.getHealthFacility().getUuid()).get());
            ronda.setRondaType(rondaTypeRepository.findByUuid(ronda.getRondaType().getUuid()).get());
            Ronda createdRonda = this.rondaRepository.update(ronda);
            updatedRondas.add(createdRonda);
        }
        return updatedRondas;
    }
}
