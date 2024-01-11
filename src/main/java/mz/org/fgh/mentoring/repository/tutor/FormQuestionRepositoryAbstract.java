package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Repository;
import io.micronaut.transaction.annotation.TransactionalAdvice;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public abstract class FormQuestionRepositoryAbstract implements FormQuestionRepository {
    private final EntityManager entityManager;
    public FormQuestionRepositoryAbstract(final EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @TransactionalAdvice
    @Override
    public List<FormQuestion> fetchByTutor(final Tutor tutor, final LifeCycleStatus lifeCycleStatus){

        return this.entityManager.createQuery("FROM FormQuestion fq INNER JOIN FETCH fq.form f " +
                "						INNER JOIN FETCH f.programmaticArea pa " +
                "						INNER JOIN pa.tutorProgrammaticAreas tpa " +
                "						INNER JOIN tpa.tutor t " +
                "						INNER JOIN f.partiner fp " +
                "						INNER JOIN FETCH fq.question q " +
                "						INNER JOIN FETCH q.questionsCategory " +
                "WHERE fq.lifeCycleStatus = :lifeCycleStatus " +
                "		AND tpa.lifeCycleStatus = :lifeCycleStatus " +
                "		AND t.uuid = :tutorUuid " +
                "		AND (fp.uuid = :partnerUUID OR fp.uuid = :MISAUUUUID)", FormQuestion.class)
                .setParameter("lifeCycleStatus", lifeCycleStatus)
                .setParameter("tutorUuid", tutor.getUuid())
                .setParameter("partnerUUID", tutor.getEmployee().getPartner().getUuid())
                .setParameter("MISAUUUUID", "398f0ffeb8fe11edafa10242ac120002")
                .getResultList();
    }
}
