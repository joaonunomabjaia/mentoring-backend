package mz.org.fgh.mentoring.controller.tutorprogrammaticarea;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.professionalCategory.ProfessionalCategoryDTO;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.dto.tutorProgrammaticArea.TutorProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.service.tutorprogrammaticarea.TutorProgrammaticAreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.TUTOR_PROGRAMMATIC_AREAS)
public class TutorProgrammaticareaController extends BaseController {

    public TutorProgrammaticareaController() {
    }

    public static final Logger LOG = LoggerFactory.getLogger(TutorProgrammaticareaController.class);

    @Inject
    private TutorProgrammaticAreaService tutorProgrammaticAreaService;
    @Operation(summary = "Save tutorprogrammaticarea to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "TutorProgrammaticArea")
    @Post("/save")
    public HttpResponse<RestAPIResponse> create (@Body TutorProgrammaticAreaDTO tutorProgrammaticAreaDTO, Authentication authentication) {
        System.out.println(tutorProgrammaticAreaDTO);
        TutorProgrammaticArea tutorProgrammaticArea = new TutorProgrammaticArea(tutorProgrammaticAreaDTO);
        tutorProgrammaticArea = this.tutorProgrammaticAreaService.create(tutorProgrammaticArea, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Created tutorProgrammaticArea {}", tutorProgrammaticArea);

        return HttpResponse.ok().body(new TutorProgrammaticAreaDTO(tutorProgrammaticArea));
    }

    @Patch(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public TutorProgrammaticArea update(@Body TutorProgrammaticArea tutorProgrammaticArea, Authentication authentication){
        return this.tutorProgrammaticAreaService.update(tutorProgrammaticArea, (Long) authentication.getAttributes().get("userInfo"));
    }

    @Operation(summary = "Return a list off all TutorProgrammaticarea")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "TutorProgrammaticarea")
    @Get("/all")
    public List<TutorProgrammaticAreaDTO> getAll() {
        return tutorProgrammaticAreaService.findAllTutorProgrammaticAreas();
    }

    @Operation(summary = "Get TutorProgrammaticarea from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "TutorProgrammaticarea")
    @Get("/{id}")
    public TutorProgrammaticAreaDTO findTutorProgrammaticareaById(@PathVariable("id") Long id){

        TutorProgrammaticArea tutorProgrammaticArea = this.tutorProgrammaticAreaService.findById(id);
        return new TutorProgrammaticAreaDTO(tutorProgrammaticArea);
    }
}
