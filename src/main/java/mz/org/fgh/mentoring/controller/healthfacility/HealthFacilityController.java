package mz.org.fgh.mentoring.controller.healthfacility;

import io.micronaut.core.annotation.NonNull;
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
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.healthFacility.HealthFacilityDTO;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.location.District;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.QuestionCategory;
import mz.org.fgh.mentoring.service.healthfacility.HealthFacilityService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.HEALTH_FACILITY)
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
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Tag(name = "HealthFacilities")
    @Get("/getall")
    public List<HealthFacilityDTO> getAll(@Nullable @QueryValue("limit") Long limit ,
                                          @Nullable @QueryValue("offset") Long offset) {
        return this.healthFacilityService.getAll(limit, offset);
    }

    @Operation(summary = "Return a list off all HealthFacilities")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "HealthFacilities")
    @Get("/getByDistricts")
    public List<HealthFacilityDTO> getByDistricts(@QueryValue("uuids") List<String> uuids) {
        List<HealthFacility> healthFacilities =  this.healthFacilityService.getByDistricts(uuids);
        if (Utilities.listHasElements((ArrayList<?>) healthFacilities)) {
            try {
                return Utilities.parseList(healthFacilities, HealthFacilityDTO.class);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
    @Operation(summary = "Return a list off all HealthFacilities")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "HealthFacilities")
    @Get("getAllOfMentor/{uuid}")
    public List<HealthFacilityDTO> getAllOfMentor(@NonNull @PathVariable("uuid") String uuid,
                                                  @Nullable @QueryValue("limit") Long limit ,
                                                  @Nullable @QueryValue("offset") Long offset) {
        return this.healthFacilityService.getAllOfMentor(uuid, limit, offset);
    }

    @Operation(summary = "Return a list off all HealthFacilities of specified Province")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "HealthFacility")
    @Get("getAllOfProvince/{provinceId}")
    public List<HealthFacilityDTO> getAllOfProvince(@PathVariable("provinceId") Long provinceId) {
        return this.healthFacilityService.findAllOfProvince(provinceId);
    }

    @Operation(summary = "Get HealthFacility from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "HealthFacility")
    @Get("/{id}")
    public HealthFacilityDTO findHealthFacilityById(@PathVariable("id") Long id){
        return new HealthFacilityDTO( this.healthFacilityService.findById(id));
    }

    @Operation(summary = "Save HealthFacility to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "HealthFacility")
    @Post("/save")
    public HttpResponse<RestAPIResponse> create (@Body HealthFacilityDTO healthFacilityDTO, Authentication authentication) {

        HealthFacility healthFacility = new HealthFacility(healthFacilityDTO);
        healthFacility = this.healthFacilityService.create(healthFacility, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Created program {}", healthFacility);

        return HttpResponse.ok().body(new HealthFacilityDTO(healthFacility));
    }

    @Operation(summary = "Update HealthFacility to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "HealthFacility")
    @Patch("/update")
    public HttpResponse<RestAPIResponse> update (@Body HealthFacilityDTO healthFacilityDTO, Authentication authentication) {

        HealthFacility healthFacility = this.healthFacilityService.findById(healthFacilityDTO.getId());
        healthFacility.setHealthFacility(healthFacilityDTO.getHealthFacility());
        healthFacility.setDistrict(new District(healthFacilityDTO.getDistrictDTO()));
        this.healthFacilityService.update(healthFacility, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Updated healthFacility {}", healthFacility);

        return HttpResponse.ok().body(healthFacility);
    }
}