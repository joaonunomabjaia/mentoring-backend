package mz.org.fgh.mentoring.controller.tutorprogrammaticarea;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
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
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.tutorProgrammaticArea.TutorProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.service.tutorprogrammaticarea.TutorProgrammaticAreaService;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
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
    private TutorRepository tutorRepository;
    @Inject
    private TutorProgrammaticAreaService tutorProgrammaticAreaService;
    @Operation(summary = "Save tutorprogrammaticarea to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "TutorProgrammaticArea")
    @Post("/save")
    public HttpResponse<RestAPIResponse> create (@Body TutorProgrammaticAreaDTO tutorProgrammaticAreaDTO, Authentication authentication) {
        try {
            TutorProgrammaticArea tutorProgrammaticArea = new TutorProgrammaticArea(tutorProgrammaticAreaDTO);
            tutorProgrammaticArea.setTutor(tutorRepository.findById(tutorProgrammaticAreaDTO.getMentorId()).get());
            tutorProgrammaticArea = this.tutorProgrammaticAreaService.create(tutorProgrammaticArea, (Long) authentication.getAttributes().get("userInfo"));

            LOG.info("Created tutorProgrammaticArea {}", tutorProgrammaticArea);

            return HttpResponse.created(new TutorProgrammaticAreaDTO(tutorProgrammaticArea));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
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

    @Operation(summary = "Update the tutorProgrammaticArea LifeCicleStatus")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Patch("/changeLifeCicleStatus")
    @Tag(name = "TutorProgrammaticArea")
    public TutorProgrammaticAreaDTO changeLifeCicleStatus(@NonNull @Body TutorProgrammaticAreaDTO tutorProgrammaticAreaDTO, Authentication authentication){
        TutorProgrammaticArea tpa = tutorProgrammaticAreaDTO.toTutorProgrammaticArea();
        tpa.setLifeCycleStatus(LifeCycleStatus.valueOf(tutorProgrammaticAreaDTO.getLifeCycleStatus()));
        TutorProgrammaticArea tutorProgrammaticArea = this.tutorProgrammaticAreaService.updateLifeCycleStatus(tpa, (Long) authentication.getAttributes().get("userInfo"));
        return new TutorProgrammaticAreaDTO(tutorProgrammaticArea);
    }
}
