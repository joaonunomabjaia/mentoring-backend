package mz.org.fgh.mentoring.repository.mentorship;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.mentorship.IterationType;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.PerformedSession;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface MentorshipRepository extends CrudRepository<Mentorship, Long> {

    @Override
    List<Mentorship> findAll();

    @Override
    Optional<Mentorship> findById(@NotNull Long id);

    List<PerformedSession> getSelectedOfFilterPMQTRList(Date startDate, Date endDate);

    List<Mentorship> fetchBySelectedFilter(String code, String tutor, String tutored, String formName, String healthFacility, IterationType type, Integer iterationNumber, LifeCycleStatus lfStatus, Date performedStartDate, Date performedEndDate);

}
