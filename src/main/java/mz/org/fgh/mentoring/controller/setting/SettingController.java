package mz.org.fgh.mentoring.controller.setting;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
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
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.setting.SettingDTO;
import mz.org.fgh.mentoring.service.setting.SettingService;

import java.util.List;
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.SETTING_CONTROLLER)
public class SettingController extends BaseController {

    @Inject
    private SettingService settingService;

    public SettingController() {
    }

    @Operation(summary = "Return a list off all Settings")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Setting")
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("/getall")
    public List<SettingDTO> getAll(@Nullable @QueryValue("limit") Long limit ,
                                   @Nullable @QueryValue("offset") Long offset) {
        return settingService.findAll(limit, offset);
    }


    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("/check")
    public HttpResponse<RestAPIResponse> check(){
        return HttpResponse.ok();
    }
}
