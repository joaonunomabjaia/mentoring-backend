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
import mz.org.fgh.mentoring.service.setting.SettingService;
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

import static mz.org.fgh.mentoring.config.SettingKeys.SERVER_BASE_URL;

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

    private final SessionRecommendedResourceRepository sessionRecommendedResourceRepository;
    private final ApplicationConfiguration applicationConfiguration;
    private final SettingService settings;

    public SessionRecommendedResourceService(SessionRecommendedResourceRepository sessionRecommendedResourceRepository, ApplicationConfiguration applicationConfiguration, SettingService settings) {
        this.sessionRecommendedResourceRepository = sessionRecommendedResourceRepository;
        this.applicationConfiguration = applicationConfiguration;
        this.settings = settings;
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
    public void processPendingResources() throws Exception {
        List<SessionRecommendedResource> pendingResources =
                sessionRecommendedResourceRepository.findByNotificationStatus(SessionRecommendedResource.NotificationStatus.PENDING);
        if (!Utilities.listHasElements(pendingResources)) return;


        // Agrupar por mentee (usar ID para evitar colisões de nomes iguais)
        Map<Long, EmailPayload> groupedByMentee = new HashMap<>();

        for (SessionRecommendedResource resource : pendingResources) {
            // Gera token para o mentee (atributo usado no seu template/link)
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("nuit", resource.getTutored().getEmployee().getNuit());
            String tokenForTutored = jwtTokenGenerator.generateToken(attributes)
                    .orElseThrow(() -> new RuntimeException("Could not generate JWT token for mentee"));

            resource.setToken(tokenForTutored);

            String link = applicationConfiguration.getBaseUrl()
                    + "/resources/load/documento?nuit=" + resource.getTutored().getEmployee().getNuit()
                    + "&token=" + resource.getToken()
                    + "&fileName=" + resource.getResourceName();

            Long menteeId = resource.getTutored().getId();

            EmailPayload payload = groupedByMentee.computeIfAbsent(
                    menteeId,
                    id -> new EmailPayload(
                            resource.getTutored().getEmployee().getFullName(),
                            resource.getTutor().getEmployee().getFullName(),
                            resource.getTutored().getEmployee().getEmail()
                    )
            );
            payload.links.add(link);

            // Atualiza o recurso como notificado
            resource.setNotificationStatus(SessionRecommendedResource.NotificationStatus.SENT);
            resource.setNotificationDate(DateUtils.getCurrentDate());
            sessionRecommendedResourceRepository.update(resource);
        }

        // Enviar um e-mail por mentee com todos os links
        for (EmailPayload payload : groupedByMentee.values()) {
            String htmlTemplate = emailService.loadHtmlTemplate("emailNotificationRecomendeResource");

            Document doc = Jsoup.parse(htmlTemplate);
            doc.getElementsByTag("ul").append(buildLinksListHtml(payload.links));

            Map<String, String> variables = new HashMap<>();
            variables.put("serverUrl", settings.get(SERVER_BASE_URL, "https://mentdev.csaude.org.mz"));
            variables.put("menteesName", payload.menteeName);
            variables.put("mentorName", payload.tutorName);

            String populatedHtml = emailService.populateTemplateVariables(doc.html(), variables);
            emailService.sendEmail(payload.email, "Acesso aos Recursos de Ensino e Aprendizagem", populatedHtml);
        }
    }

    private String buildLinksListHtml(List<String> links) {
        StringBuilder sb = new StringBuilder();
        for (String url : links) {
            String[] parts = url.split("fileName=", 2);
            String fileName = parts.length == 2 ? parts[1] : url;
            sb.append("<li><a href=\"").append(url).append("\">").append(fileName).append("</a></li>");
        }
        return sb.toString();
    }

    // Estrutura simples para consolidar dados do e-mail
    private static class EmailPayload {
        final String menteeName;
        final String tutorName;
        final String email;
        final List<String> links = new ArrayList<>();

        EmailPayload(String menteeName, String tutorName, String email) {
            this.menteeName = menteeName;
            this.tutorName = tutorName;
            this.email = email;
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
