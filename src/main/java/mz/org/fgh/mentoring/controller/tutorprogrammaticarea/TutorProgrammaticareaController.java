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
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.service.tutorprogrammaticarea.TutorProgrammaticAreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller(RESTAPIMapping.TUTOR_PROGRAMMATIC_AREAS)
public class TutorProgrammaticareaController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(TutorProgrammaticareaController.class);

    @Inject
    private TutorProgrammaticAreaService tutorProgrammaticAreaService;
    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public TutorProgrammaticArea create(@Body TutorProgrammaticArea tutorProgrammaticArea, Authentication authentication){
         return this.tutorProgrammaticAreaService.create(tutorProgrammaticArea, (Long) authentication.getAttributes().get("userInfo"));
    }

//    @Put(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
//    public TutorProgrammaticArea update(@Body TutorProgrammaticArea tutorProgrammaticArea){
//        return this.tutorProgrammaticAreaService.update(tutorProgrammaticArea);
//    }
//
//    @Operation(summary = "Return a list off all TutorProgrammaticarea")
//    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
//    @Tag(name = "TutorProgrammaticarea")
//    @Get("/all")
//    public List<TutorProgrammaticAreaDTO> getAll() {
//        return tutorProgrammaticAreaService.findAllTutorProgrammaticAreas();
//    }
//
//    @Operation(summary = "Save TutorProgrammaticarea to database")
//    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
//    @Tag(name = "TutorProgrammaticarea")
//    @Post("/save")
//    public HttpResponse<RestAPIResponse> create (@Body TutorProgrammaticAreaDTO tutorProgrammaticAreaDTO, Authentication authentication) {
//
//        TutorProgrammaticArea tutorProgrammaticArea = new TutorProgrammaticArea(tutorProgrammaticAreaDTO);
//        tutorProgrammaticArea = this.tutorProgrammaticAreaService.create(tutorProgrammaticArea, (Long) authentication.getAttributes().get("userInfo"));
//
//        LOG.info("Created tutorProgrammaticArea {}", tutorProgrammaticArea);
//
//        return HttpResponse.ok().body(new TutorProgrammaticAreaDTO(tutorProgrammaticArea));
//    }
//
//    @Operation(summary = "Get TutorProgrammaticarea from database")
//    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
//    @Tag(name = "TutorProgrammaticarea")
//    @Get("/{id}")
//    public TutorProgrammaticAreaDTO findProfessionalCategoryById(@PathVariable("id") Long id){
//
//        TutorProgrammaticArea professionalCategory = this.tutorProgrammaticAreaService.findById(id).get();
//        return new TutorProgrammaticAreaDTO(professionalCategory);
//    }
//
//    @Operation(summary = "Update TutorProgrammaticarea to database")
//    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
//    @Tag(name = "TutorProgrammaticarea")
//    @Patch("/update")
//    public HttpResponse<RestAPIResponse> update (@Body TutorProgrammaticAreaDTO professionalCategoryDTO, Authentication authentication) {
//
//        TutorProgrammaticArea professionalCategory = this.tutorProgrammaticAreaService.findById(professionalCategoryDTO.getId()).get();
//        professionalCategory.setTutor(professionalCategoryDTO.getTutorDTO());
//        professionalCategory.setProgrammaticArea(professionalCategoryDTO.getProgrammaticAreaDTO());
//        professionalCategory = this.tutorProgrammaticAreaService.update(professionalCategory, (Long) authentication.getAttributes().get("userInfo"));
//
//        LOG.info("Updated professionalCategory {}", professionalCategory);
//
//        return HttpResponse.ok().body(new TutorProgrammaticAreaDTO(professionalCategory));
//    }
}
