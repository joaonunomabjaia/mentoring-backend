package mz.org.fgh.mentoring.controller.setting;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.setting.SettingDTO;
import mz.org.fgh.mentoring.service.setting.SettingService;

import java.util.List;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Controller(RESTAPIMapping.SETTING_CONTROLLER)
public class SettingController extends BaseController {

    @Inject
    private SettingService settingService;

    public SettingController() {
    }

    @Operation(summary = "Return a list off all Settings")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Version(API_VERSION)
    @Get("/setting/{limit}/{offset}")
    public List<SettingDTO> getAll(@PathVariable("limit") long limit , @PathVariable("offset") long offset) {
        return settingService.findAll(limit, offset);
    }

    @Get("/tutor/{uuid}")
    public List<SettingDTO> findSettingByTutor(@PathVariable("uuid") String uuid){
        return this.settingService.findSettingByTutor(uuid);
    }
}
