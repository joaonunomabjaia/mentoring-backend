package mz.org.fgh.mentoring.controller.location;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
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
import mz.org.fgh.mentoring.api.PaginatedResponse;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.SuccessResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.LifeCycleStatusDTO;
import mz.org.fgh.mentoring.dto.location.CabinetDTO;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.service.location.CabinetService;
import mz.org.fgh.mentoring.util.Utilities;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jose Julai Ritsure
 */
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.CABINET_CONTROLLER)
public class CabinetController extends BaseController {

    @Inject
    private CabinetService cabinetService;

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Return a list off all Cabinets")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Cabinet")
    @Get("/getall")
    public List<CabinetDTO> getAll(@Nullable @QueryValue("limit") Long limit ,
                                   @Nullable @QueryValue("offset") Long offset) {
        return cabinetService.findAllCabinets(limit, offset);
    }

    @Operation(summary = "List or search Cabinets (paginated)")
    @Get
    public HttpResponse<?> listOrSearch(@Nullable @QueryValue("name") String name,
                                        @Nullable Pageable pageable) {

        Page<Cabinet> data = !Utilities.stringHasValue(name)
                ? cabinetService.findAll(resolvePageable(pageable))
                : cabinetService.searchByName(name, resolvePageable(pageable));

        List<CabinetDTO> dataDTOs = data.getContent().stream()
                .map(CabinetDTO::new)
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

    @Operation(summary = "Create a new Cabinet")
    @Post
    public HttpResponse<?> create(@Body CabinetDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        Cabinet cabinet = new Cabinet(dto);
        cabinet.setCreatedBy(userUuid);
        Cabinet created = cabinetService.create(cabinet);
        return HttpResponse.created(SuccessResponse.of("Sector criado com sucesso", new CabinetDTO(created)));
    }

    @Operation(summary = "Update an existing Cabinet")
    @Put
    public HttpResponse<?> update(@Body CabinetDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        Cabinet cabinet = new Cabinet(dto);
        cabinet.setUpdatedBy(userUuid);
        Cabinet updated = cabinetService.update(cabinet);
        return HttpResponse.ok(SuccessResponse.of("Sector atualizado com sucesso", new CabinetDTO(updated)));
    }

    @Operation(summary = "Activate or deactivate a Cabinet by changing its LifeCycleStatus")
    @Put("/{uuid}/status")
    public HttpResponse<?> updateLifeCycleStatus(@PathVariable String uuid,
                                                 @Body LifeCycleStatusDTO dto,
                                                 Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        Cabinet updated = cabinetService.updateLifeCycleStatus(uuid, dto.getLifeCycleStatus(), userUuid);
        return HttpResponse.ok(SuccessResponse.of("Estado do sector atualizado com sucesso", new CabinetDTO(updated)));
    }

    @Operation(summary = "Delete a Cabinet by UUID")
    @Delete("/{uuid}")
    public HttpResponse<?> delete(@PathVariable String uuid, Authentication authentication) {
        cabinetService.delete(uuid);
        return HttpResponse.ok(SuccessResponse.messageOnly("Sector eliminado com sucesso"));
    }
}
