package mz.org.fgh.mentoring.controller.professionalcategory;

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
import mz.org.fgh.mentoring.api.PaginatedResponse;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.SuccessResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.LifeCycleStatusDTO;
import mz.org.fgh.mentoring.dto.professionalCategory.ProfessionalCategoryDTO;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.service.professionalcategory.ProfessionalCategoryService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.PROFESSIONAL_CATEGORIES)
public class ProfessionalCategoryController extends BaseController {

    public static final Logger LOG = LoggerFactory.getLogger(ProfessionalCategoryController.class);

    private final ProfessionalCategoryService professionalCategoryService;

    public ProfessionalCategoryController(ProfessionalCategoryService professionalCategoryService) {
        this.professionalCategoryService = professionalCategoryService;
    }

    @Operation(summary = "Return a list off all Professional Categories")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "ProfessionalCategory")
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("getall")
    public List<ProfessionalCategoryDTO> getAll(@Nullable @QueryValue("limit") Long limit ,
                                                @Nullable @QueryValue("offset") Long offset) {
        return this.professionalCategoryService.getAll(limit, offset);
    }


    @Operation(summary = "Get professionalCategory from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "ProfessionalCategory")
    @Get("/{id}")
    public ProfessionalCategoryDTO findProfessionalCategoryById(@PathVariable("id") Long id){

        ProfessionalCategory professionalCategory = this.professionalCategoryService.findById(id).get();
        return new ProfessionalCategoryDTO(professionalCategory);
    }


    @Operation(summary = "List or search professional categories (paginated)")
    @Get
    public HttpResponse<?> listOrSearch(@Nullable @QueryValue("name") String name,
                                        @Nullable Pageable pageable) {

        Page<ProfessionalCategory> data = !Utilities.stringHasValue(name)
                ? professionalCategoryService.findAll(resolvePageable(pageable))
                : professionalCategoryService.searchByName(name, resolvePageable(pageable));

        List<ProfessionalCategoryDTO> dataDTOs = data.getContent().stream()
                .map(ProfessionalCategoryDTO::new)
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

    @Operation(summary = "Create a new ProfessionalCategory")
    @Post
    public HttpResponse<?> create(@Body ProfessionalCategoryDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("userUuid");
        ProfessionalCategory category = new ProfessionalCategory(dto);
        category.setCreatedBy(userUuid);
        ProfessionalCategory created = professionalCategoryService.create(category);
        return HttpResponse.created(SuccessResponse.of("Categoria profissional criada com sucesso", new ProfessionalCategoryDTO(created)));
    }


    @Operation(summary = "Update an existing ProfessionalCategory")
    @Put
    public HttpResponse<?> update(@Body ProfessionalCategoryDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        ProfessionalCategory category = new ProfessionalCategory(dto);
        category.setUpdatedBy(userUuid);
        ProfessionalCategory updated = professionalCategoryService.update(category);
        return HttpResponse.ok(SuccessResponse.of("Categoria profissional atualizada com sucesso", new ProfessionalCategoryDTO(updated)));
    }

    @Operation(summary = "Activate or deactivate a ProfessionalCategory by changing its LifeCycleStatus")
    @Put("/{uuid}/status")
    public HttpResponse<?> updateLifeCycleStatus(@PathVariable String uuid, @Body LifeCycleStatusDTO dto, Authentication authentication) {
        ProfessionalCategory updated = professionalCategoryService.updateLifeCycleStatus(uuid, dto.getLifeCycleStatus(), (String) authentication.getAttributes().get("useruuid"));
        return HttpResponse.ok(SuccessResponse.of("Estado da categoria profissional atualizado com sucesso", new ProfessionalCategoryDTO(updated)));
    }

    @Operation(summary = "Delete a ProfessionalCategory by UUID")
    @Delete("/{uuid}")
    public HttpResponse<?> delete(@PathVariable String uuid, Authentication authentication) {
        professionalCategoryService.delete(uuid);
        return HttpResponse.ok(SuccessResponse.messageOnly("Categoria profissional eliminada com sucesso"));
    }


}
