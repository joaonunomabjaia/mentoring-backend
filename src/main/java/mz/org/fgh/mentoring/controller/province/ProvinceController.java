package mz.org.fgh.mentoring.controller.province;

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
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.province.ProvinceDTO;
import mz.org.fgh.mentoring.service.location.ProviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("province")
public class ProvinceController extends BaseController {

    public static final Logger LOG = LoggerFactory.getLogger(ProvinceController.class);

    private final ProviceService proviceService;

    public ProvinceController(ProviceService proviceService) {
        this.proviceService = proviceService;
    }

    @Operation(summary = "Return a list off all Provinces")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Province")
    @Get("getall")
    public List<ProvinceDTO> getAll(@Nullable @QueryValue("limit") Long limit,
                                    @Nullable @QueryValue("offset") Long offset) {
        return this.proviceService.getAll(limit, offset);
    }
}
