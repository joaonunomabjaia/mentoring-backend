package mz.org.fgh.mentoring.controller.location;

import io.micronaut.http.annotation.Controller;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.service.healthfacility.HealthFacilityService;

/**
 * @author Jose Julai Ritsure
 */
@Controller(RESTAPIMapping.CABINET_CONTROLLER)
public class HealthFacilityController extends BaseController {

    @Inject
    private HealthFacilityService healthFacilityService;
}
