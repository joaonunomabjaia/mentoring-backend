package mz.org.fgh.mentoring.controller.Partner;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.controller.tutor.TutorController;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.service.partner.PartnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Controller(RESTAPIMapping.PARTNER)
public class PartnerController extends BaseController {

    private PartnerService partnerService;

    public static final Logger LOG = LoggerFactory.getLogger(TutorController.class);

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @Operation(summary = "Return a list off all Partner")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Partner")
    @Version(API_VERSION)
    @Get
    public List<Partner> getAll() {
        LOG.debug("Searching tutors version 2");
        return this.partnerService.findAllPartners();
    }

    @Get
    public List<Partner> getAll1() {
        LOG.debug("Searching tutors version 2");
        return this.partnerService.findAllPartners();
    }

    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public Partner create (@Body Partner partner) {

         Partner partnerResult = this.partnerService.createPartner(partner);

        LOG.debug("Created tutor {}", partnerResult);
        return partnerResult;
    }

    @Put(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public Partner updatePartner(@Body Partner partner){

        return this.partnerService.updatePartner(partner);
    }
}
