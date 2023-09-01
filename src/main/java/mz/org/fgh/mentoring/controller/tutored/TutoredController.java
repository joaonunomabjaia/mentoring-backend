package mz.org.fgh.mentoring.controller.tutored;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.service.tutored.TutoredService;

import java.util.ArrayList;
import java.util.List;

@Controller(RESTAPIMapping.TUTORED_CONTROLLER)
public class TutoredController extends BaseController {

    @Inject
    TutoredService tutoredService;

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Tutored")
    @Get
    public List<TutoredDTO> getAll() {
        List<TutoredDTO> tutoredDTOS = tutoredService.findAll();
        return tutoredDTOS;
    }
}
