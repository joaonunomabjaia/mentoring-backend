package mz.org.fgh.mentoring.controller.district;

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
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.district.DistrictDTO;
import mz.org.fgh.mentoring.service.district.DistrictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("district")
public class DistrictController extends BaseController {

    public static final Logger LOG = LoggerFactory.getLogger(DistrictController.class);

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @Operation(summary = "Return a list off all Districts of a specified Province ")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "District")
    @Get("getAllOfProvince/{provinceId}")
    public List<DistrictDTO> getAllOfProvince(@PathVariable("provinceId") Long provinceId) {
        return this.districtService.getAllOfProvince(provinceId);
    }

    @Operation(summary = "Return a list off all Districts")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Districts")
    @Get("/getall")
    public List<DistrictDTO> getAll(@Nullable @QueryValue("limit") Long limit ,
                                    @Nullable @QueryValue("offset") Long offset) {
        return this.districtService.getAll(limit, offset);
    }

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Districts")
    @Get("/districts")
    public List<DistrictDTO> getAll() {
        return this.districtService.getAll();
    }
}
