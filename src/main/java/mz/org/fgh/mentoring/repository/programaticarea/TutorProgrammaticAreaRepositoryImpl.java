package mz.org.fgh.mentoring.repository.programaticarea;

import io.micronaut.data.annotation.Repository;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public abstract class TutorProgrammaticAreaRepositoryImpl implements TutorProgrammaticAreaRepository {

    public TutorProgrammaticArea createOrUpdate(TutorProgrammaticArea tutorProgrammaticArea, User user, Tutor tutor) {
        Optional<TutorProgrammaticArea> possibleTutorProgrammaticAra = findByUuid(tutorProgrammaticArea.getUuid());

        if (possibleTutorProgrammaticAra.isPresent()) {
            tutorProgrammaticArea.setId(possibleTutorProgrammaticAra.get().getId());
            tutorProgrammaticArea.setUpdatedBy(user.getUuid());
            tutorProgrammaticArea.setUpdatedAt(DateUtils.getCurrentDate());
            tutorProgrammaticArea.setTutor(possibleTutorProgrammaticAra.get().getTutor());
            return update(tutorProgrammaticArea);
        } else {
            tutorProgrammaticArea.setCreatedBy(user.getUuid());
            tutorProgrammaticArea.setUuid(UUID.randomUUID().toString());
            tutorProgrammaticArea.setCreatedAt(DateUtils.getCurrentDate());
            tutorProgrammaticArea.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
            tutorProgrammaticArea.setTutor(tutor);
            return save(tutorProgrammaticArea);
        }
    }

}
