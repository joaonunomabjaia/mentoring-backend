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
import mz.org.fgh.mentoring.dto.programmaticarea.TutorProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.service.tutorprogrammaticarea.TutorProgrammaticAreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.TUTOR_PROGRAMMATIC_AREAS)
public class TutorProgrammaticareaController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(TutorProgrammaticareaController.class);

    @Inject
    private TutorProgrammaticAreaService tutorProgrammaticAreaService;
    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public TutorProgrammaticArea create(@Body TutorProgrammaticArea tutorProgrammaticArea, Authentication authentication){
         return this.tutorProgrammaticAreaService.create(tutorProgrammaticArea, (Long) authentication.getAttributes().get("userInfo"));
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
