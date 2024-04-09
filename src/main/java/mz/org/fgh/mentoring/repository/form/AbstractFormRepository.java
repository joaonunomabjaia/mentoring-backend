package mz.org.fgh.mentoring.repository.form;

import io.micronaut.data.annotation.Repository;
import mz.org.fgh.mentoring.base.AbstaractBaseRepository;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.user.User;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
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

    @Transactional
    @Override
    public List<Form> search(final String code, final String name, final String programmaticArea) {

        String sql = "SELECT DISTINCT(f.id) FROM forms f " +
                " INNER JOIN form_type ft ON f.FORM_TYPE_ID = ft.ID " +
                " INNER JOIN partners p ON f.PARTNER_ID = p.ID " +
                " INNER JOIN programmatic_areas pa ON f.PROGRAMMATIC_AREA_ID = pa.ID ";

        if(code != null || name != null || programmaticArea != null) {
            sql += " WHERE 1=1 ";
        }
        if (code != null) {
            sql += " AND f.code like '%" + code + "%' ";
        }
        if (name != null) {
            sql += " AND f.name like '%" + name + "%' ";
        }
        if (programmaticArea != null) {
            sql += " AND pa.description like '%" + programmaticArea + "%' ";
        }

        //sql += addUserAuthCondition(user);

        Query qw = this.session.getCurrentSession().createSQLQuery(sql);
        //sqlQuery.setParameter("startDate", user.getId());
        List<Long> ids = qw.getResultList();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Form> criteria = builder.createQuery(Form.class);
        Root<Form> root = criteria.from(Form.class);
        criteria.select(root).where(root.get("id").in(ids));
        Query q = this.session.getCurrentSession().createQuery(criteria);
        List<Form> forms = q.getResultList();
        return forms;
    }
}
