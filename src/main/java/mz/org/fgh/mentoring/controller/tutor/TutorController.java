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
    @Get("/career/{limit}/{offset}")
    public List<Tutor> getAll(@PathVariable("limit") long limit , @PathVariable("offset") long offset) {
        LOG.debug("Searching tutors version 2");
        List<Tutor> tutors = new ArrayList<>();

        if(limit > 0){
             tutors = this.tutorService.findTutorWithLimit(limit, offset);
        }else {
            tutors =  tutorService.findAll();
        }
        return tutors;
    }

    @Get
    public List<Tutor> getAllV1() {
        LOG.debug("Searching tutors version 1");
        return tutorService.findAll();
    }

    @Get("/{id}")
    public Tutor findTutorById(@PathVariable("id") Long id){
       return this.tutorService.findById(id).get();
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
