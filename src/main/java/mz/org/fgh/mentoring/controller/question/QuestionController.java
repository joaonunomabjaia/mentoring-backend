package mz.org.fgh.mentoring.controller.question;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.service.question.QuestionService;

import java.util.List;

@Controller("/questions")
public class QuestionController extends BaseController {

    @Inject
    private QuestionService questionService;

    public QuestionController() {
    }

    @Get("/{formCode}")
    public List<Question> getByFormCode(@PathVariable String formCode) {
        return questionService.getQuestionsByFormCode(formCode);
    }
}
