package mz.org.fgh.mentoring.service.question;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.question.QuestionCategoryDTO;
import mz.org.fgh.mentoring.entity.question.QuestionCategory;
import mz.org.fgh.mentoring.repository.question.QuestionsCategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class QuestionCategoryService {

    @Inject
    QuestionsCategoryRepository questionsCategoryRepository;

    public List<QuestionCategoryDTO> findAll(){
        List<QuestionCategory> questionsCategories = this.questionsCategoryRepository.findAll();
        List<QuestionCategoryDTO> dtos = new ArrayList<>();
        for (QuestionCategory questionCategory : questionsCategories) {
            QuestionCategoryDTO dto = new QuestionCategoryDTO(questionCategory);
            dtos.add(dto);
        }
        return dtos;
    }

}
