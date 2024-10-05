package mz.org.fgh.mentoring.service.session;

import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.config.ApplicationConfiguration;
import mz.org.fgh.mentoring.dto.session.SessionRecommendedResourceDTO;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.session.SessionRecommendedResource;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.session.SessionRecommendedResourceRepository;
import mz.org.fgh.mentoring.repository.settings.SettingsRepository;
import mz.org.fgh.mentoring.service.tutor.TutorService;
import mz.org.fgh.mentoring.service.tutored.TutoredService;
import mz.org.fgh.mentoring.service.user.UserService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import mz.org.fgh.util.EmailService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class SessionRecommendedResourceService {

    @Inject
    private SessionService sessionService;
    @Inject
    private TutoredService tutoredService;
    @Inject
    private TutorService tutorService;
    @Inject
    private UserService userService;
    @Inject
    private EmailService emailService;
    @Inject
    private JwtTokenGenerator jwtTokenGenerator;

    @Inject
    private SettingsRepository settingsRepository;

    private final SessionRecommendedResourceRepository sessionRecommendedResourceRepository;
    private final ApplicationConfiguration applicationConfiguration;

    public SessionRecommendedResourceService(SessionRecommendedResourceRepository sessionRecommendedResourceRepository, ApplicationConfiguration applicationConfiguration) {
        this.sessionRecommendedResourceRepository = sessionRecommendedResourceRepository;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Transactional
    public SessionRecommendedResource save(SessionRecommendedResource sessionRecommendedResource, Long userId) {
        User user = userService.findById(userId);
        sessionRecommendedResource.setCreatedAt(DateUtils.getCurrentDate());
        sessionRecommendedResource.setCreatedBy(user.getUuid());
        sessionRecommendedResource.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        return sessionRecommendedResourceRepository.save(sessionRecommendedResource);
    }

    @Transactional
    public void delete(Long id) {
        sessionRecommendedResourceRepository.deleteById(id);
    }

    @Transactional
    public List<SessionRecommendedResource> findAll() {
        Iterable<SessionRecommendedResource> iterable = sessionRecommendedResourceRepository.findAll();
        List<SessionRecommendedResource> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    @Transactional
    public SessionRecommendedResource findByTutoredAndResourceLink(Long tutoredId, String resourceLink) {
        return sessionRecommendedResourceRepository.findByTutoredIdAndResourceLink(tutoredId, resourceLink);
    }

    public SessionRecommendedResource createEntityFromDto(SessionRecommendedResourceDTO dto) {
        Session session = sessionService.findByUuid(dto.getSessionUuid());
        Tutored tutored = tutoredService.findByUuid(dto.getTutoredUuid());
        Tutor tutor = tutorService.findByUuid(dto.getTutorUuid());

        return new SessionRecommendedResource(dto, session, tutored, tutor);
    }

    @Transactional
    public SessionRecommendedResource updateByUuid(String uuid, SessionRecommendedResource updatedResource, Long userId) {
        User user = userService.findById(userId);
        return sessionRecommendedResourceRepository.findByUuid(uuid).map(existingResource -> {
            existingResource.setResourceLink(updatedResource.getResourceLink());
            existingResource.setResourceName(updatedResource.getResourceName());
            existingResource.setSession(updatedResource.getSession());
            existingResource.setTutored(updatedResource.getTutored());
            existingResource.setTutor(updatedResource.getTutor());
            existingResource.setDateRecommended(updatedResource.getDateRecommended());
            existingResource.setNotificationDate(updatedResource.getNotificationDate());
            existingResource.setNotificationStatus(updatedResource.getNotificationStatus());
            existingResource.setUpdatedAt(DateUtils.getCurrentDate());
            existingResource.setUpdatedBy(user.getUuid());
            return sessionRecommendedResourceRepository.update(existingResource);
        }).orElseThrow(() -> new IllegalArgumentException("No SessionRecommendedResource found with UUID: " + uuid));
    }

    @Transactional
    public SessionRecommendedResource findByTutoredUuidAndResourceLink(String tutoredUuid, String resourceLink) {
        return sessionRecommendedResourceRepository.findByTutoredUuidAndResourceLink(tutoredUuid, resourceLink)
                .orElseThrow(() -> new IllegalArgumentException("No SessionRecommendedResource found for the provided tutored UUID and resource link"));
    }

    @Transactional
    public void processPendingResources() throws MessagingException {
        List<SessionRecommendedResource> pendingResources = sessionRecommendedResourceRepository.findByNotificationStatus(SessionRecommendedResource.NotificationStatus.PENDING);
        if (!Utilities.listHasElements(pendingResources)) return;

        List<String> menteesNames = new ArrayList<>();

        List<String> links = new ArrayList<>();

        Map<SessionRecommendedResource, List<String>> variableList = new HashMap<>();

        Optional<Setting> settingOpt = settingsRepository.findByDesignation("SERVER_URL");

        if (!settingOpt.isPresent()) {
            throw new RuntimeException("Server URL not configured");
        }

        try {
        for (SessionRecommendedResource resource : pendingResources) {

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("nuit", resource.getTutored().getEmployee().getNuit());

            String tokenForTutored = this.jwtTokenGenerator.generateToken(attributes).get();
            resource.setToken(tokenForTutored);

            if(menteesNames.contains(resource.getTutored().getEmployee().getFullName())){
                String link = this.applicationConfiguration.getBaseUrl()+"/resources/load/documento?nuit="+resource.getTutored().getEmployee().getNuit()+"&token="+resource.getToken()+"&fileName="+resource.getResourceName();

                variableList.get(resource).add(link);
            }else {
                String link = this.applicationConfiguration.getBaseUrl()+"/resources/load/documento?nuit="+resource.getTutored().getEmployee().getNuit()+"&token="+resource.getToken()+"&fileName="+resource.getResourceName();
                links = new ArrayList<>();
                links.add(link);
                variableList.put(resource, links);
            }

            menteesNames.add(resource.getTutored().getEmployee().getFullName());

            resource.setNotificationStatus(SessionRecommendedResource.NotificationStatus.SENT);
            sessionRecommendedResourceRepository.update(resource); // Update the status to SENT
        }

        for(Map.Entry<SessionRecommendedResource, List<String>> entry : variableList.entrySet()){

            String htmlTampleteResult = emailService.loadHtmlTemplate("emailNotificationRecomendeResource");

            String results = htmlString(entry.getValue());

            Document htm = Jsoup.parse(htmlTampleteResult);

            htm.getElementsByTag("ul").append(results);

            Map<String, String> variables = new HashMap<>();
            variables.put("serverUrl", settingOpt.get().getValue());
            variables.put("menteesName", entry.getKey().getTutored().getEmployee().getFullName());
            variables.put("mentorName", entry.getKey().getTutor().getEmployee().getFullName());

            String populationHtml = emailService.populateTemplateVariables(htm.html(), variables);

            emailService.sendEmail(entry.getKey().getTutored().getEmployee().getEmail(), "Acesso aos Recursos de Ensino e Aprendizagem", populationHtml);

        }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<SessionRecommendedResource> saveMany(List<SessionRecommendedResource> resources, Long userId) {
        List<SessionRecommendedResource> list = new ArrayList<>();
        for (SessionRecommendedResource resource : resources) {
            list.add(this.save(resource, userId));
        }
        return list;
    }

    private String htmlString(List<String> uls){

        String HTMLSTring = "";
        for(String ul : uls){
            String [] resourseName = ul.split("fileName=");
            HTMLSTring+="<li> <a href="+ul+">"+resourseName[1]+"</a></li>";
        }
        return  HTMLSTring;
    }
}
