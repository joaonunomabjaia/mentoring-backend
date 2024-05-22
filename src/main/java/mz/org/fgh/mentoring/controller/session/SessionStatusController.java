package mz.org.fgh.mentoring.controller.session;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.session.SessionStatusDTO;
import mz.org.fgh.mentoring.entity.session.SessionStatus;
import mz.org.fgh.mentoring.service.session.SessionStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.SESSION_STATUS)
public class SessionStatusController extends BaseController {

    @Inject
    private SessionStatusService sessionStatusService;

    public static final Logger LOG = LoggerFactory.getLogger(SessionStatusController.class);
    public SessionStatusController() {
    }

    @Operation(summary = "Return a list off all Session Statuses")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "SessionStatus")
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("/getall")
    public List<SessionStatusDTO> getAll() {
        List<SessionStatus> sessionStatuses = sessionStatusService.findAll();
        List<SessionStatusDTO> sessionStatusDTOS = new ArrayList<>();
        for(SessionStatus sessionStatus : sessionStatuses){
            sessionStatusDTOS.add(new SessionStatusDTO(sessionStatus));
        }
        return sessionStatusDTOS;
    }

}
