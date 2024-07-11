package mz.org.fgh.mentoring.controller.session;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.fgh.mentoring.dto.session.SessionDTO;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.service.session.SessionService;

import java.util.ArrayList;
import java.util.List;

@Controller(RESTAPIMapping.SESSIONS)
@Secured(SecurityRule.IS_AUTHENTICATED)
public class SessionController extends BaseController {

    @Inject
    SessionService sessionService;

    @Operation(summary = "Return a list of all Sessions of given Rondas Uuids")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Session")
    @Get("/getAllOfRondas")
    public List<SessionDTO> getAllRondas(@QueryValue("rondasUuids") List<String> rondasUuids) {
        List<Session> sessions = this.sessionService.getAllRondas(rondasUuids);
        List<SessionDTO> sessionDTOS = new ArrayList<>();
        for (Session session: sessions) {
            SessionDTO sessionDTO = new SessionDTO(session);
            List<MentorshipDTO> mentorshipDTOList = composeMentorships(session);
            sessionDTO.setMentorships(mentorshipDTOList);
            sessionDTOS.add(sessionDTO);
        }
        return sessionDTOS;
    }

    private List<MentorshipDTO> composeMentorships(Session session) {
        List<MentorshipDTO> dtos = new ArrayList<>();
        for (Mentorship mentorship: session.getMentorships()) {
            MentorshipDTO mentorshipDTO = new MentorshipDTO(mentorship);
            dtos.add(mentorshipDTO);
        }
        return dtos;
    }
}
