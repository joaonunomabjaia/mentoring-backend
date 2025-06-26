package mz.org.fgh.mentoring.controller.partner;

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
import mz.org.fgh.mentoring.dto.partner.PartnerDTO;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.service.partner.PartnerService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.PARTNER)
public class PartnerController extends BaseController {

    private final PartnerService partnerService;

    public static final Logger LOG = LoggerFactory.getLogger(PartnerController.class);

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @Operation(summary = "Return a list off all Partners")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Partner")
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("getall")
    public HttpResponse<?> getAll(
            Pageable pageable
    ) {
        try {
            return HttpResponse.ok(partnerService.findAllPartners(pageable));
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


    @Operation(summary = "Get Partner from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Partner")
    @Get("/{id}")
    public PartnerDTO findTartnerById(@PathVariable("id") Long id){
        Partner partner = this.partnerService.getById(id);
        return  new PartnerDTO(partner);
    }


    @Operation(summary = "List or search partners (paginated)")
    @Get
    public HttpResponse<?> listOrSearch(@Nullable @QueryValue("name") String name,
                                        @Nullable Pageable pageable) {

        Page<Partner> data = !Utilities.stringHasValue(name)
                ? partnerService.findAll(resolvePageable(pageable))
                : partnerService.searchByName(name, resolvePageable(pageable));

        List<PartnerDTO> dataDTOs = data.getContent().stream()
                .map(PartnerDTO::new)
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

    @Operation(summary = "Create a new Partner")
    @Post
    public HttpResponse<?> create(@Body PartnerDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        Partner partner = new Partner(dto);
        partner.setCreatedBy(userUuid);
        Partner created = partnerService.create(partner);
        return HttpResponse.created(SuccessResponse.of("Parceiro criado com sucesso", new PartnerDTO(created)));
    }

    @Operation(summary = "Update an existing Partner")
    @Put
    public HttpResponse<?> update(@Body PartnerDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        Partner partner = new Partner(dto);
        partner.setUpdatedBy(userUuid);
        Partner updated = partnerService.update(partner);
        return HttpResponse.ok(SuccessResponse.of("Parceiro atualizado com sucesso", new PartnerDTO(updated)));
    }

    @Operation(summary = "Activate or deactivate a Partner by changing its LifeCycleStatus")
    @Put("/{uuid}/status")
    public HttpResponse<?> updateLifeCycleStatus(@PathVariable String uuid, @Body LifeCycleStatusDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        Partner updated = partnerService.updateLifeCycleStatus(uuid, dto.getLifeCycleStatus(), userUuid);
        return HttpResponse.ok(SuccessResponse.of("Estado do parceiro atualizado com sucesso", new PartnerDTO(updated)));
    }

    @Operation(summary = "Delete a Partner by UUID")
    @Delete("/{uuid}")
    public HttpResponse<?> delete(@PathVariable String uuid, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        partnerService.delete(uuid);
        return HttpResponse.ok(SuccessResponse.messageOnly("Parceiro eliminado com sucesso"));
    }

}
