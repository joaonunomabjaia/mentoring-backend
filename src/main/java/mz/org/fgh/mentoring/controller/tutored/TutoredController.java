package mz.org.fgh.mentoring.controller.tutored;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.service.tutored.TutoredService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.TUTORED_CONTROLLER)
public class TutoredController extends BaseController {

    @Inject
    TutoredService tutoredService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TutoredController.class);

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Tutored")
    @Get("/{limit}/{offset}")
    public List<TutoredDTO> getAll(@PathVariable("limit") long limit, @PathVariable("offset") long offset) {
        List<TutoredDTO> tutoredDTOS = tutoredService.findAll(limit, offset);
        return tutoredDTOS;
    }

    @Get("/tutor/{tutorUuid}")
    public List<TutoredDTO> getTutoredByTutorUuid(@PathVariable("tutorUuid") String tutorUuid){

        LOGGER.debug(" get Tutored By Tutor Uuid {}", tutorUuid);
        List<TutoredDTO> tutoredDTOS = this.tutoredService.findTutorByUserUuid(tutorUuid);
        return tutoredDTOS;
    }

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Tutored")
    @Get("/tutored/{uuid}")
    public List<TutoredDTO> getTutoredByUuid(@PathVariable("uuid") String tutorUuid){

        List<TutoredDTO> tutoredDTOS = this.tutoredService.findTutoredByUuid(tutorUuid);
        return tutoredDTOS;
    }

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "mentorados")
    @Get("/search")
    public List<TutoredDTO> searchTutored(@NonNull @QueryValue("userId") Long userId,
                                          @Nullable @QueryValue("nuit") Long nuit,
                                          @Nullable @QueryValue("name") String name,
                                          @Nullable @QueryValue("phoneNumber") String phoneNumber) {


        List<TutoredDTO> tutorDTOS = new ArrayList<>();

        tutorDTOS =  tutoredService.searchTutored(userId, nuit, name, phoneNumber);

        return tutorDTOS;
    }
    @Operation(summary = "Save Mentorados to database")
    @Tag(name = "mentorados")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Put("/update")
    public TutoredDTO updateTutored(@Body TutoredDTO tutoredDTO , Authentication authentication){

        TutoredDTO respo = this.tutoredService.updateTutored(tutoredDTO, (Long) authentication.getAttributes().get("userInfo"));

        return respo;
    }
}
