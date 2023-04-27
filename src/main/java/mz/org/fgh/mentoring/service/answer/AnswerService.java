package mz.org.fgh.mentoring.service.answer;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.repository.tutor.AnswerRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.List;
import java.util.Optional;

@Singleton
public class AnswerService {

    @Inject
    AnswerRepository answerRepository;

    public List<Answer> findAll(){
      return   this.answerRepository.findAll();
    }
    public Optional<Answer> findById(Long id){
        return this.answerRepository.findById(id);
    }

    public List<Answer> fetchByMentorishipUuid(final String mentorshipUuid ){

        return this.answerRepository.fetchByMentorishipUuid(mentorshipUuid, LifeCycleStatus.ACTIVE);
    }

    public Answer create(Answer answer){
       return this.answerRepository.save(answer);
    }
}
