package mz.org.fgh.mentoring.controller.tutorprogrammaticarea;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.service.tutorprogrammaticarea.TutorProgrammaticAreaService;

@Controller(RESTAPIMapping.TUTOR_PROGRAMMATIC_AREAS)
public class TutorProgrammaticarea extends BaseController {

    @Inject
    private TutorProgrammaticAreaService tutorProgrammaticAreaService;
    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public TutorProgrammaticArea create(@Body TutorProgrammaticArea tutorProgrammaticArea){
         return this.tutorProgrammaticAreaService.create(tutorProgrammaticArea);
    }

    @Put(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public TutorProgrammaticArea update(@Body TutorProgrammaticArea tutorProgrammaticArea){
        return this.tutorProgrammaticAreaService.update(tutorProgrammaticArea);
    }
}
