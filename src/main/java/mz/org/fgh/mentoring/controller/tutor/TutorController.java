package mz.org.fgh.mentoring.controller.tutor;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
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
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Controller(RESTAPIMapping.TUTOR_CONTROLLER)
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
    @Get("/tutors/{limit}/{offset}")
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

    @Get
    public List<TutorDTO> getAllV1() {
        LOG.debug("Searching tutors version 1");

        List<Tutor> tutors = new ArrayList<>();
        List<TutorDTO> tutorDTOS = new ArrayList<>();

        tutors = tutorService.findAll();

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

    @Get("/user/{userUuid}")
    public TutorDTO findTutorByUserUuid(@PathVariable("userUuid") String userUuid){

        Tutor tutor = this.tutorService.findTutorByUserUuid(userUuid);
        return new TutorDTO(tutor);
    }


    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public HttpResponse<RestAPIResponse> create (@Body Tutor tutor) {


        LOG.debug("Created tutor {}", new Tutor());

        return HttpResponse.ok().body(new Tutor());
    }
}