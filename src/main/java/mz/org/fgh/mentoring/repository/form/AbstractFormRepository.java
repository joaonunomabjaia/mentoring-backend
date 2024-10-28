package mz.org.fgh.mentoring.repository.form;

import io.micronaut.data.annotation.Repository;
import mz.org.fgh.mentoring.base.AbstaractBaseRepository;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import org.hibernate.SessionFactory;

import org.hibernate.query.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jose Julai Ritsure
 */
@Repository
public abstract class AbstractFormRepository extends AbstaractBaseRepository implements FormRepository {

    private final SessionFactory session;

    protected AbstractFormRepository(SessionFactory session) {
        this.session = session;
    }

    @Override
    public List<Form> getAllOfTutor(Tutor tutor) {
        // Define the HQL query to fetch forms based on the tutor ID
        String hql = "select f from Form f " +
                "join f.programmaticArea pa " +
                "join pa.tutorProgrammaticAreas tpa " +
                "join tpa.tutor t " +
                "where t.id = :tutorId";

        // Create the query and set the parameter for tutor ID
        Query<Form> query = this.session.openSession().createQuery(hql, Form.class);
        query.setParameter("tutorId", tutor.getId());

        // Execute the query and retrieve the list of forms
        List<Form> forms = query.getResultList();

        return forms;
    }

}
