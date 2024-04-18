package mz.org.fgh.mentoring.controller.tutored;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.service.tutored.TutoredService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.TUTORED_CONTROLLER)
public class TutoredController extends BaseController {

    @Inject
    TutoredService tutoredService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TutoredController.class);

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get("/{offset}/{limit}")
    public List<TutoredDTO> getAll(@PathVariable("offset") long offset, @PathVariable("limit") long limit) {
        List<TutoredDTO> tutoredDTOS = tutoredService.findAll(offset, limit);
        return tutoredDTOS;
    }

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get("/tutoreds")
    public List<TutoredDTO> getTutoredAll() {
        List<TutoredDTO> tutoredDTOs = new ArrayList<>();

        tutoredDTOs =  tutoredService.searchTutored(4l, null, null, null);
        return tutoredDTOs;
    }

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Tutored")
    @Get("getTutoreds")
    public List<TutoredDTO> getTutoreds(@QueryValue("uuids") List<String> uuids) {
        List<Tutored> tutoreds = tutoredService.getTutoredsByHealthFacilityUuids(uuids);
        try {
            return Utilities.parseList(tutoreds, TutoredDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
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


        List<TutoredDTO> tutoredDTOs = new ArrayList<>();

        tutoredDTOs =  tutoredService.searchTutored(userId, nuit, name, phoneNumber);

        return tutoredDTOs;
    }
    @Operation(summary = "Save Mentorados to database")
    @Tag(name = "mentorados")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Put("/update")
    public TutoredDTO updateTutored(@Body TutoredDTO tutoredDTO , Authentication authentication){

        TutoredDTO respo = this.tutoredService.updateTutored(tutoredDTO, (Long) authentication.getAttributes().get("userInfo"));

        return respo;
    }


    @Operation(summary = "Save Mentee to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Mentee")
    @Post("/save")
    public HttpResponse<RestAPIResponse> create (@Body TutoredDTO tutoredDTO, Authentication authentication) {

        try {
            Tutored tutored = new Tutored(tutoredDTO);


            this.tutoredService.create(tutored, (Long) authentication.getAttributes().get("userInfo"));

            return HttpResponse.ok().body(new TutoredDTO(tutored));
        } catch (PersistenceException e) {
            Throwable rootCause = e.getCause();
            if (rootCause instanceof SQLException) {
                SQLException constraintViolationEx = (SQLException) rootCause;

                String errorMessage = constraintViolationEx.getMessage();
                String constraintName = extractConstraintName(errorMessage);
                if (constraintName != null) {
                    LOGGER.error("Violated constraint name: " + constraintName);
                } else {
                    LOGGER.error("No violated constraint name available.");
                }
                LOGGER.error("Constraint violation error: " + errorMessage);
                // Handle the ConstraintViolationException as needed
            } else {
                // Handle other types of exceptions or re-throw the original exception
                e.printStackTrace();
            }
            LOGGER.info("Error on saving mentees: {}", e.getMessage());
            return HttpResponse.badRequest(
                                    MentoringAPIError.builder()
                                                    .status(HttpStatus.BAD_REQUEST.getCode())
                                                    .error(e.getLocalizedMessage())
                                                    .message(e.getMessage()).build());
        }
    }

    private String extractConstraintName(String errorMessage) {
        String[] parts = errorMessage.split(" ");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].startsWith("constraint")) {
                return parts[i];
            }
        }
        return null;
    }
}
