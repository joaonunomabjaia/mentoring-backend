package mz.org.fgh.mentoring.controller.program;

import io.micronaut.core.annotation.NonNull;
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
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.service.program.ProgramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jose Julai Ritsure
 */
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller(RESTAPIMapping.PROGRAM)
public class ProgramController extends BaseController {
    private ProgramService programService;

    public static final Logger LOG = LoggerFactory.getLogger(ProgramController.class);

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @Operation(summary = "Return a list off all Programs")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Program")
    @Get("/getAll")
    public List<ProgramDTO> getAll() {
        return programService.findAllPrograms();
    }

    @Operation(summary = "Save Program to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Program")
    @Post("/save")
    public HttpResponse<RestAPIResponse> create (@Body ProgramDTO programDTO, Authentication authentication) {

        Program program = new Program(programDTO);
        program = this.programService.create(program, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Created program {}", program);

        return HttpResponse.ok().body(new ProgramDTO(program));
    }

    @Operation(summary = "Get Program from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Program")
    @Get("/{id}")
    public ProgramDTO findProgramById(@PathVariable("id") Long id){

        Program program = this.programService.findById(id).get();
        return new ProgramDTO(program);
    }

    @Operation(summary = "Update Program to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Program")
    @Patch("/update")
    public HttpResponse<RestAPIResponse> update (@Body ProgramDTO programDTO, Authentication authentication) {

        Program program = this.programService.findById(programDTO.getId()).get();
        program.setDescription(programDTO.getDescription());
        program.setName(programDTO.getName());
        program = this.programService.update(program, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Updated program {}", program);

        return HttpResponse.ok().body(new ProgramDTO(program));
    }
}
