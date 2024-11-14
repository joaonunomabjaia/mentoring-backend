package mz.org.fgh.mentoring.controller.programmaticArea;

import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.service.programaticarea.ProgramaticAreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.PROGRAMMATIC_AREAS)
public class ProgramaticAreaController extends BaseController {

    private ProgramaticAreaService programaticAreaService;
    public static final Logger LOG = LoggerFactory.getLogger(ProgramaticAreaController.class);

    public ProgramaticAreaController(ProgramaticAreaService programaticAreaService) {
        this.programaticAreaService = programaticAreaService;
    }

    @Operation(summary = "Return a list off all ProgramaticArea")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "ProgramaticArea")
    @Get("/getAll")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<?> getAll(
            Pageable pageable
    ) {
        try {
            return HttpResponse.ok(programaticAreaService.fetchAllProgrammaticAreas(pageable));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(
                    MentoringAPIError.builder()
                            .status(HttpStatus.BAD_REQUEST.getCode())
                            .error(e.getLocalizedMessage())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @Get
    public List<ProgrammaticAreaDTO> findProgrammaticAreas(final String code, final String name){

       return this.programaticAreaService.findProgrammaticAreas(code, name);
    }

//    @Get("/tutor-progammatic-area/{tutorUuid}")
//    public List<ProgrammaticAreaDTO> findProgrammaticAreaByTutorProgrammaticAreaUuid(@PathVariable("tutorUuid") final String tutorUuid){
//      return this.programaticAreaService.findProgrammaticAreaByTutorProgrammaticAreaUuid(tutorUuid);
//    }

    @Post(  consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    public ProgrammaticArea create(@Body ProgrammaticArea programaticArea, Authentication authentication){

        return this.programaticAreaService.createProgrammaticArea(programaticArea, (Long) authentication.getAttributes().get("userInfo"));
    }

    @Patch(   consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    public ProgrammaticArea update(@Body ProgrammaticArea programaticArea, Authentication authentication){

        return this.programaticAreaService.updateProgrammaticArea(programaticArea, (Long) authentication.getAttributes().get("userInfo"));
    }

    @Operation(summary = "Return a list of programmatic areas by Program")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "ProgrammaticArea")
    @Get("/getbyprogram")
    public List<ProgrammaticAreaDTO> getProgrammaticAreasByProgramId( @QueryValue("program") Long programId){
        return this.programaticAreaService.findProgrammaticAreasByProgramId(programId);
    }

        @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Get ProgrammaticArea from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "ProgrammaticArea")
    @Patch("/{id}")
    public ProgrammaticAreaDTO deleteProgrammaticArea(@PathVariable("id") Long id, Authentication authentication){

        ProgrammaticArea programmaticArea = this.programaticAreaService.getProgrammaticAreaById(id);        
        programmaticArea = this.programaticAreaService.delete(programmaticArea, (Long) authentication.getAttributes().get("userInfo"));       

        return new ProgrammaticAreaDTO(programmaticArea);
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Get ProgrammaticArea from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "ProgrammaticArea")
    @Delete("/{id}")
    public ProgrammaticAreaDTO destroyProgrammaticArea(@PathVariable("id") Long id, Authentication authentication){

        ProgrammaticArea programmaticArea = this.programaticAreaService.getProgrammaticAreaById(id);        
        this.programaticAreaService.destroy(programmaticArea);       

        return new ProgrammaticAreaDTO(programmaticArea);
    }

}
