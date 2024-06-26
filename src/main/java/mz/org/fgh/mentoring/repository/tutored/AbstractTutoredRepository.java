package mz.org.fgh.mentoring.repository.tutored;


import io.micronaut.data.annotation.Repository;
import mz.org.fgh.mentoring.base.AbstaractBaseRepository;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
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

    @Override
    public List<Tutored> getTutoredsByHealthFacilityUuids(List<String> uuids) {
        Session sess = null;
        List<Tutored> tutoreds = new ArrayList<>();
        try {
            sess = this.session.openSession();
            // HQL/JPQL query to directly fetch Tutored entities
            String hql = "select distinct t FROM Tutored t " +
                        "join t.employee e " +
                        "join e.locations l " +
                        "join l.district d " +
                        "join d.healthFacilities hf " +
                        "where l.lifeCycleStatus = 'ACTIVE' " +
                        "and hf.lifeCycleStatus = 'ACTIVE' " +
                        "and hf.uuid in :uuids";

            Query query = sess.createQuery(hql, Tutored.class);
            query.setParameter("uuids", uuids);

            tutoreds = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // Logging the exception is better practice
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
        return tutoreds;
    }
}
