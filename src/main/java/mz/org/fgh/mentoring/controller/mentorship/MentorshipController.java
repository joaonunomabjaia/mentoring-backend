package mz.org.fgh.mentoring.controller.mentorship;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.service.mentorship.MentorshipService;
import mz.org.fgh.mentoring.service.session.SessionService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller(RESTAPIMapping.MENTORSHIP)
@Secured(SecurityRule.IS_AUTHENTICATED)
public class MentorshipController extends BaseController {

    public static final Logger LOG = LoggerFactory.getLogger(MentorshipController.class);

    @Inject
    MentorshipService mentorshipService;

    @Inject
    SessionService sessionService;


    @Operation(summary = "Creates a Collection of mentorships records")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Mentorship")
    @Post("/save")
    public List<MentorshipDTO> saveMentorships(@Body List<MentorshipDTO> mentorshipDTOS, Authentication authentication) {
        List<Mentorship> mentorships = Utilities.parse(mentorshipDTOS, Mentorship.class);
        List<Mentorship> savedMentorships = mentorshipService.createMentorships(mentorships, (Long) authentication.getAttributes().get("userInfo"));
        return listAsDtos(savedMentorships, MentorshipDTO.class);
    }
}
