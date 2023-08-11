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
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.setting.SettingDTO;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.service.setting.SettingService;
import mz.org.fgh.mentoring.service.tutor.TutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Controller(RESTAPIMapping.SETTING_CONTROLLER)
public class SettingController extends BaseController {

    @Inject
    private SettingService settingService;

    public SettingController() {
    }

    public static final Logger LOG = LoggerFactory.getLogger(SettingController.class);

    @Operation(summary = "Return a list off all Tutor")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Tutor")
    @Version(API_VERSION)
    @Get
    public List<SettingDTO> getAll() {
        LOG.debug("Searching tutors version 2");
        return settingService.findAll();
    }

    @Get
    public List<SettingDTO> getAllV1() {
        LOG.debug("Searching tutors version 1");
        return settingService.findAll();
    }

    @Get("/tutor/{uuid}")
    public List<SettingDTO> findSettingByTutor(@PathVariable("uuid") String uuid){
        return this.settingService.findSettingByTutor(uuid);
    }
}
