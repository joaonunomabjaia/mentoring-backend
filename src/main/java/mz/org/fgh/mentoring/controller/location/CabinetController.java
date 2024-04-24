package mz.org.fgh.mentoring.controller.location;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.location.CabinetDTO;
import mz.org.fgh.mentoring.service.location.CabinetService;

import java.util.List;

/**
 * @author Jose Julai Ritsure
 */
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
}
