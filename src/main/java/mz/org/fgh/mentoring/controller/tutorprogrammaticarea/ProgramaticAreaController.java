package mz.org.fgh.mentoring.controller.tutorprogrammaticarea;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.controller.tutor.TutorController;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.service.programaticarea.ProgramaticAreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Controller(RESTAPIMapping.PROGRAMMATIC_AREAS)
public class ProgramaticAreaController extends BaseController {

    private ProgramaticAreaService programaticAreaService;
    public static final Logger LOG = LoggerFactory.getLogger(TutorController.class);

    public ProgramaticAreaController(ProgramaticAreaService programaticAreaService) {
        this.programaticAreaService = programaticAreaService;
    }

    @Operation(summary = "Return a list off all ProgramaticArea")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "ProgramaticArea")
    @Get("/programmaticareas/{limit}/{offset}")
    public List<ProgrammaticAreaDTO> getAll(@PathVariable("limit") long limit, @PathVariable("offset") long offset) {
        LOG.debug("Searching tutors version 2");
        return programaticAreaService.findProgrammaticAreasAll(limit, offset);
    }

    @Get
    public List<ProgrammaticAreaDTO> findProgrammaticAreas(final String code, final String name){

       return this.programaticAreaService.findProgrammaticAreas(code, name);
    }

    @Get("/tutor-progammatic-area/{tutorUuid}")
    public List<ProgrammaticAreaDTO> findProgrammaticAreaByTutorProgrammaticAreaUuid(@PathVariable("tutorUuid") final String tutorUuid){
      return this.programaticAreaService.findProgrammaticAreaByTutorProgrammaticAreaUuid(tutorUuid);
    }

    @Post(  consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    public ProgrammaticArea create(@Body ProgrammaticArea programaticArea){

        return this.programaticAreaService.createProgrammaticArea(programaticArea);
    }

    @Put(   consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    public ProgrammaticArea update(@Body ProgrammaticArea programaticArea){

        return this.programaticAreaService.updateProgrammaticArea(programaticArea);
    }
}
