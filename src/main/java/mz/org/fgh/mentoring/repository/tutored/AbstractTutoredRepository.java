package mz.org.fgh.mentoring.repository.tutored;


import io.micronaut.data.annotation.Repository;
import mz.org.fgh.mentoring.base.AbstaractBaseRepository;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public abstract class AbstractTutoredRepository extends AbstaractBaseRepository implements TutoredRepository {

    private final SessionFactory session;

    public AbstractTutoredRepository(SessionFactory session) {
        this.session = session;
    }

    @Transactional
    @Override
    public List<Tutored> search(Long nuit, String name, User user, String phoneNumber) {

        String sql = "SELECT t.id " + "FROM tutoreds t INNER JOIN employee e ON t.EMPLOYEE_ID = e.ID " +
                     "where 1=1";

        if ( name != null){
            sql += " AND e.name like '%" + name + "%' ";
        }
        if (nuit != null){
            sql += " AND e.nuit like '%" + nuit + "%' ";
        }
        if ( phoneNumber != null ) {
            sql += " AND e.phone_number like '%" + phoneNumber + "%' ";
        }

        sql += addUserAuthCondition(user);

        Query qw = this.session.getCurrentSession().createSQLQuery(sql);

        List<Long> ids = qw.getResultList();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Tutored> criteria = builder.createQuery(Tutored.class);
        Root<Tutored> root = criteria.from(Tutored.class);
        criteria.select(root).where(root.get("id").in(ids));
        Query q = this.session.getCurrentSession().createQuery(criteria);
        List<Tutored> tutoreds =  q.getResultList();

        return tutoreds;
    }
}
