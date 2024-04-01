package mz.org.fgh.mentoring.controller.healthfacility;

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
import mz.org.fgh.mentoring.dto.healthFacility.HealthFacilityDTO;
import mz.org.fgh.mentoring.service.healthfacility.HealthFacilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("healthfacility")
public class HealthFacilityController extends BaseController {

    public static final Logger LOG = LoggerFactory.getLogger(HealthFacilityController.class);

    private final HealthFacilityService healthFacilityService;

    public HealthFacilityController(HealthFacilityService healthFacilityService) {
        this.healthFacilityService = healthFacilityService;
    }

    @Operation(summary = "Return a list off all HealthFacilities of specified District")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "HealthFacility")
    @Get("getAllOfDistrict/{districtId}")
    public List<HealthFacilityDTO> getAllOfDistrict(@PathVariable("districtId") Long districtId) {
        return this.healthFacilityService.findAllOfDistrict(districtId);
    }

    @Operation(summary = "Return a list off all HealthFacilities")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "HealthFacilities")
    @Get("/getall")
    public List<HealthFacilityDTO> getAll(@Nullable @QueryValue("limit") Long limit ,
                                          @Nullable @QueryValue("offset") Long offset) {
        return this.healthFacilityService.getAll(limit, offset);
    }

    @Operation(summary = "Return a list off all HealthFacilities of specified Province")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "HealthFacility")
    @Get("getAllOfProvince/{provinceId}")
    public List<HealthFacilityDTO> getAllOfProvince(@PathVariable("provinceId") Long provinceId) {
        return this.healthFacilityService.findAllOfProvince(provinceId);
    }
}
