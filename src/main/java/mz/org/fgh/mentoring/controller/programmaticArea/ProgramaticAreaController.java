package mz.org.fgh.mentoring.controller.programmaticArea;

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
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.service.programaticarea.ProgramaticAreaService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

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

    @Operation(summary = "Return a list of programmatic areas by Program")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "ProgrammaticArea")
    @Get("/getbyprogram")
    public List<ProgrammaticAreaDTO> getProgrammaticAreasByProgramId( @QueryValue("program") Long programId){
        return this.programaticAreaService.findProgrammaticAreasByProgramId(programId);
    }

    @Operation(summary = "List or search programatic areas (paginated)")
    @Get
    public HttpResponse<?> listOrSearch(@Nullable @QueryValue("name") String name,
                                        @Nullable Pageable pageable) {

        Page<ProgrammaticArea> data = !Utilities.stringHasValue(name)
                ? programaticAreaService.findAll(resolvePageable(pageable))
                : programaticAreaService.searchByName(name, resolvePageable(pageable));

        List<ProgrammaticAreaDTO> dataDTOs = data.getContent().stream()
                .map(ProgrammaticAreaDTO::new)
                .collect(Collectors.toList());

        String message = data.getTotalSize() == 0
                ? "Sem Dados para esta pesquisa"
                : "Dados encontrados";

        return HttpResponse.ok(
                PaginatedResponse.of(
                        dataDTOs,
                        data.getTotalSize(),
                        data.getPageable(),
                        message
                )
        );
    }

    @Operation(summary = "Create a new ProgrammaticArea")
    @Post
    public HttpResponse<?> create(@Body ProgrammaticAreaDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        ProgrammaticArea area = new ProgrammaticArea(dto);
        area.setCreatedBy(userUuid);
        ProgrammaticArea created = programaticAreaService.create(area);
        return HttpResponse.created(SuccessResponse.of("Área programática criada com sucesso", new ProgrammaticAreaDTO(created)));
    }

    @Operation(summary = "Update an existing ProgrammaticArea")
    @Put
    public HttpResponse<?> update(@Body ProgrammaticAreaDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        ProgrammaticArea area = new ProgrammaticArea(dto);
        area.setUpdatedBy(userUuid);
        ProgrammaticArea updated = programaticAreaService.update(area);
        return HttpResponse.ok(SuccessResponse.of("Área programática atualizada com sucesso", new ProgrammaticAreaDTO(updated)));
    }

    @Operation(summary = "Activate or deactivate a ProgrammaticArea by changing its LifeCycleStatus")
    @Put("/{uuid}/status")
    public HttpResponse<?> updateLifeCycleStatus(@PathVariable String uuid, @Body LifeCycleStatusDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        ProgrammaticArea updated = programaticAreaService.updateLifeCycleStatus(uuid, dto.getLifeCycleStatus(), userUuid);
        return HttpResponse.ok(SuccessResponse.of("Estado da área programática atualizado com sucesso", new ProgrammaticAreaDTO(updated)));
    }

    @Operation(summary = "Delete a ProgrammaticArea by UUID")
    @Delete("/{uuid}")
    public HttpResponse<?> delete(@PathVariable String uuid, Authentication authentication) {
        programaticAreaService.delete(uuid);
        return HttpResponse.ok(SuccessResponse.messageOnly("Área programática eliminada com sucesso"));
    }

}
