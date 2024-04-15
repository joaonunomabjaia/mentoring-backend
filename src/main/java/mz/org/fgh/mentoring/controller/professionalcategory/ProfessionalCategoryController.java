package mz.org.fgh.mentoring.controller.professionalcategory;

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
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.professionalCategory.ProfessionalCategoryDTO;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.service.professionalcategory.ProfessionalCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("professionalcategory")
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

    @Operation(summary = "Save ProfessionalCategory to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "ProfessionalCategory")
    @Post("/save")
    public HttpResponse<RestAPIResponse> create (@Body ProfessionalCategoryDTO professionalCategoryDTO, Authentication authentication) {

        ProfessionalCategory professionalCategory = new ProfessionalCategory(professionalCategoryDTO);
        professionalCategory = this.professionalCategoryService.create(professionalCategory, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Created professionalCategory {}", professionalCategory);

        return HttpResponse.ok().body(new ProfessionalCategoryDTO(professionalCategory));
    }

    @Operation(summary = "Get professionalCategory from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "ProfessionalCategory")
    @Get("/{id}")
    public ProfessionalCategoryDTO findProfessionalCategoryById(@PathVariable("id") Long id){

        ProfessionalCategory professionalCategory = this.professionalCategoryService.findById(id).get();
        return new ProfessionalCategoryDTO(professionalCategory);
    }

    @Operation(summary = "Update professionalCategory to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "ProfessionalCategory")
    @Patch("/update")
    public HttpResponse<RestAPIResponse> update (@Body ProfessionalCategoryDTO professionalCategoryDTO, Authentication authentication) {

        ProfessionalCategory professionalCategory = this.professionalCategoryService.findById(professionalCategoryDTO.getId()).get();
        professionalCategory.setDescription(professionalCategoryDTO.getDescription());
        professionalCategory.setCode(professionalCategoryDTO.getCode());
        professionalCategory = this.professionalCategoryService.update(professionalCategory, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Updated professionalCategory {}", professionalCategory);

        return HttpResponse.ok().body(new ProfessionalCategoryDTO(professionalCategory));
    }
}
