package mz.org.fgh.mentoring.controller.tutor;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.service.tutor.TutorService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("mentor")
public class TutorController extends BaseController {

    @Inject
    private TutorService tutorService;

    public TutorController() {
    }

    public static final Logger LOG = LoggerFactory.getLogger(TutorController.class);

    @Operation(summary = "Return a list off all Tutor")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Tutor")
    @Version(API_VERSION)
    @Get("/{limit}/{offset}")
    public List<TutorDTO> getAll(@PathVariable("limit") long limit , @PathVariable("offset") long offset) {
        LOG.debug("Searching tutors version 2");
        List<Tutor> tutors = new ArrayList<>();
        List<TutorDTO> tutorDTOS = new ArrayList<>();

        if(limit > 0){
            tutors = this.tutorService.findTutorWithLimit(limit, offset);
        }else {
            tutors =  tutorService.findAll();
        }

        for (Tutor tutor : tutors){
            tutorDTOS.add(new TutorDTO(tutor));
        }
        return tutorDTOS;
    }

    @Operation(summary = "Return a list off all Tutor")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Mentor")
    @Get("/search")
    public List<TutorDTO> search(@Nullable @QueryValue("name") String name,
                                 @Nullable @QueryValue("nuit") Long nuit,
                                 @NonNull @QueryValue("userId") Long userId,
                                 @Nullable @QueryValue("phoneNumber") String phoneNumber) {
        LOG.debug("Searching tutors ....");
        List<Tutor> tutors = new ArrayList<>();
        List<TutorDTO> tutorDTOS = new ArrayList<>();

        tutors =  tutorService.search(name, nuit, userId, phoneNumber);


        for (Tutor tutor : tutors){
            tutorDTOS.add(new TutorDTO(tutor));
        }
        return tutorDTOS;
    }

    @Get
    public List<TutorDTO> getAllV1() {
        LOG.debug("Searching tutors version 1");

        List<Tutor> tutors = new ArrayList<>();
        List<TutorDTO> tutorDTOS = new ArrayList<>();

        tutors = tutorService.findAll();

        if (!Utilities.listHasElements((ArrayList<?>) tutors)) return null;
        for(Tutor tutor : tutors){
            tutorDTOS.add(new TutorDTO(tutor));
        }
        return tutorDTOS;
    }

    @Get("/{id}")
    public TutorDTO findTutorById(@PathVariable("id") Long id){

        Tutor tutor = this.tutorService.findById(id).get();
        return new TutorDTO(tutor);
    }

  /*  @Get("/user/{userUuid}")
    public TutorDTO findTutorByUserUuid(@PathVariable("userUuid") String userUuid){

        Tutor tutor = this.tutorService.findTutorByUserUuid(userUuid);
        return new TutorDTO(tutor);
    }*/

    @Operation(summary = "Save Mentor to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Mentor")
    @Post("/save")
    public HttpResponse<RestAPIResponse> create (@Body TutorDTO tutorDTO, Authentication authentication) {

        Tutor tutor = new Tutor(tutorDTO);
       tutor = this.tutorService.create(tutor, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Created mentor {}", tutor);

        return HttpResponse.ok().body(new TutorDTO(tutor));
    }


}