package mz.org.fgh.mentoring.controller.partner;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.partner.PartnerDTO;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.service.partner.PartnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.PARTNER)
public class PartnerController extends BaseController {

    private final PartnerService partnerService;

    public static final Logger LOG = LoggerFactory.getLogger(PartnerController.class);

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @Operation(summary = "Return a list off all Partners")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Partner")
    @Get("getall")
    public List<PartnerDTO> getAll(@Nullable @QueryValue("limit") Long limit ,
                                   @Nullable @QueryValue("offset") Long offset) {
        return this.partnerService.getAll(limit, offset);
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
