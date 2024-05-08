package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Repository;
import mz.org.fgh.mentoring.base.AbstaractBaseRepository;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.user.User;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public abstract class AbstractTutorRepository extends AbstaractBaseRepository implements TutorRepository{

    private final SessionFactory session;

    protected AbstractTutorRepository(SessionFactory session) {
        this.session = session;
    }

    @Transactional
    @Override
    public List<Tutor> search(String name, Long nuit, User user, String phoneNumber) {
        String sql = "SELECT t.id " +
                     "FROM tutors t INNER JOIN employee e ON t.EMPLOYEE_ID = e.ID " +
                     "where 1=1 ";

        if (name != null) {
            sql += " AND e.name like '%" + name + "%' || e.surname like '%" + name + "%' ";
        }
        if (nuit != null) {
            sql += " AND e.nuit like '%" + nuit + "%' ";
        }
        if (phoneNumber != null) {
            sql += " AND e.phoneNumber like '%" + phoneNumber + "%' ";
        }
        sql += addUserAuthCondition(user);

        Query qw = this.session.getCurrentSession().createSQLQuery(sql);
        //sqlQuery.setParameter("startDate", user.getId());
        List<Long> ids = qw.getResultList();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Tutor> criteria = builder.createQuery(Tutor.class);
        Root<Tutor> root = criteria.from(Tutor.class);
        criteria.select(root).where(root.get("id").in(ids));
        Query q = this.session.getCurrentSession().createQuery(criteria);
        List<Tutor> tutors = q.getResultList();

        return tutors;
    }
}
