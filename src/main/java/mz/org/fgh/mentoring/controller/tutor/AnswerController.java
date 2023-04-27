package mz.org.fgh.mentoring.controller.tutor;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.service.answer.AnswerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Controller(RESTAPIMapping.ANSWER_CONTROLLER)
public class AnswerController {

    @Inject
    AnswerService answerService;

    public AnswerController(){

    }

    public static final Logger LOG = LoggerFactory
            .getLogger(AnswerController.class);

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Answer")
    @Version(API_VERSION)
    @Get
    public List<Answer> getAll(){
        return this.answerService.findAll();
    }

    @Get
    public List<Answer> getAllV1(){
        LOG.debug("Searching Answer version 1");
        return answerService.findAll();
    }

    @Get("{/mentorshipUuid}")
    public List<Answer> fetchAnswersByMentorshipUuid(@PathVariable("mentorshipUuid") String mentorshipUuid){
        LOG.debug("Searching Answer By MentorshipUuid ,{}", mentorshipUuid);
        return  this.answerService.fetchByMentorishipUuid(mentorshipUuid);
    }

    @Post(consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON)
    public Answer create(@Body Answer answer){
        LOG.debug("Created answer {} ", answer);
        return this.answerService.create(answer);
    }



}
