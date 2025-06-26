package mz.org.fgh.mentoring.controller.program;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mz.org.fgh.mentoring.api.PaginatedResponse;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.SuccessResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.LifeCycleStatusDTO;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.service.program.ProgramService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

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

    @Operation(summary = "List or search programs (paginated)")
    @Get
    public HttpResponse<?> listOrSearch(@Nullable @QueryValue("name") String name,
                                        @Nullable Pageable pageable) {

        Page<Program> groups = !Utilities.stringHasValue(name)
                ? programService.findAll(resolvePageable(pageable))
                : programService.searchByName(name, resolvePageable(pageable));

        List<ProgramDTO> groupDTOs = groups.getContent().stream()
                .map(ProgramDTO::new)
                .collect(Collectors.toList());

        String message = groups.getTotalSize() == 0
                ? "Sem Dados para esta pesquisa"
                : "Dados encontrados";

        return HttpResponse.ok(
                PaginatedResponse.of(
                        groupDTOs,
                        groups.getTotalSize(),
                        groups.getPageable(),
                        message
                )
        );
    }

    @Operation(summary = "Return a list off all Programs")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Program")
    @Get("/getAll")
    public HttpResponse<?> getAll(
            Pageable pageable
    ) {
        try {
            return HttpResponse.ok(programService.findAllPrograms(pageable));
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

    @Operation(summary = "Get Program from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Program")
    @Get("/{id}")
    public ProgramDTO findProgramById(@PathVariable("id") Long id){

        Program program = this.programService.findById(id).get();
        return new ProgramDTO(program);
    }

    @Operation(summary = "Delete a group by UUID")
    @Delete("/{uuid}")
    public HttpResponse<?> delete(@PathVariable String uuid) {
        programService.delete(uuid);
        return HttpResponse.ok(SuccessResponse.messageOnly("Programa eliminado com sucesso"));
    }

    @Operation(summary = "Activate or deactivate a program by changing its LifeCycleStatus")
    @Put("/{uuid}/status")
    public HttpResponse<?> updateLifeCycleStatus(@PathVariable String uuid, @Body LifeCycleStatusDTO dto, Authentication authentication) {
        Program updatedGroup = programService.updateLifeCycleStatus(uuid, dto.getLifeCycleStatus(), (String) authentication.getAttributes().get("useruuid"));
        return HttpResponse.ok(SuccessResponse.of("Estado do Programa atualizado com sucesso", new ProgramDTO(updatedGroup)));
    }

    @Operation(summary = "Update an existing Program")
    @Put
    public HttpResponse<?> update(@Body ProgramDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        Program group = new Program(dto);
        group.setUpdatedBy(userUuid);
        Program updated = programService.update(group);
        return HttpResponse.ok(SuccessResponse.of("Programa atualizado com sucesso", new ProgramDTO(updated)));
    }

    @Operation(summary = "Create a new Program")
    @Post
    public HttpResponse<?> create(@Body ProgramDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("userUuid");
        Program group = new Program(dto);
        group.setCreatedBy(userUuid);
        Program created = programService.create(group);
        return HttpResponse.created(SuccessResponse.of("Programa criado com sucesso", new ProgramDTO(created)));
    }
}
