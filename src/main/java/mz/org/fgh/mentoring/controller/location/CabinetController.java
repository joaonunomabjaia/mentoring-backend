package mz.org.fgh.mentoring.controller.location;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
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

    @Operation(summary = "Return a list off all Cabinets")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Cabinet")
    @Get("/cabinet/{limit}/{offset}")
    public List<CabinetDTO> getAll(@PathVariable("limit") long limit , @PathVariable("offset") long offset) {
        return cabinetService.findAllCabinets(limit, offset);
    }
}
