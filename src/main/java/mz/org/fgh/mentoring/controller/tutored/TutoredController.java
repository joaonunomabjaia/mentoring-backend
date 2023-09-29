package mz.org.fgh.mentoring.controller.tutored;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.service.tutored.TutoredService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


@Controller(RESTAPIMapping.TUTORED_CONTROLLER)
public class TutoredController extends BaseController {

    @Inject
    TutoredService tutoredService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TutoredController.class);

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Tutored")
    @Get("/{limit}/{offset}")
    public List<TutoredDTO> getAll(@PathVariable("limit") long limit, @PathVariable("offset") long offset) {
        List<TutoredDTO> tutoredDTOS = tutoredService.findAll(limit, offset);
        return tutoredDTOS;
    }

    @Get("/tutor/{tutorUuid}")
    public List<TutoredDTO> getTutoredByTutorUuid(@PathVariable("tutorUuid") String tutorUuid){

        LOGGER.debug(" get Tutored By Tutor Uuid {}", tutorUuid);
        List<TutoredDTO> tutoredDTOS = this.tutoredService.findTutorByUserUuid(tutorUuid);
        return tutoredDTOS;
    }
}
