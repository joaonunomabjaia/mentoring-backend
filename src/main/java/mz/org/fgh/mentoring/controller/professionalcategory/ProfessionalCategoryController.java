package mz.org.fgh.mentoring.controller.professionalcategory;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.professionalCategory.ProfessionalCategoryDTO;
import mz.org.fgh.mentoring.service.professionalcategory.ProfessionalCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
}
