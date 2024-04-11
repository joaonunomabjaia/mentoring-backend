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
import java.util.List;
import java.util.stream.Collectors;

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

        Session sess = this.session.openSession();

        String sql = "select DISTINCT(t.ID) " +
                "from tutoreds t inner join employee e on t.EMPLOYEE_ID  = e.ID  " +
                "               inner join location l on l.EMPLOYEE_ID = e.ID  " +
                "               inner join districts d on d.ID = l.DISTRICT_ID  " +
                "               INNER  join health_facilities hf on hf.DISTRICT_ID = d.ID  " +
                "where l.LIFE_CYCLE_STATUS = 'ACTIVE' and hf.LIFE_CYCLE_STATUS = 'ACTIVE' AND hf.UUID  in ("+String.join(",", uuids.stream().map(n -> ("'" + n + "'")).collect(Collectors.toList()))+")";

        Query qw = sess.createSQLQuery(sql);

        List<Long> ids = qw.getResultList();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Tutored> criteria = builder.createQuery(Tutored.class);
        Root<Tutored> root = criteria.from(Tutored.class);
        criteria.select(root).where(root.get("id").in(ids));
        Query q = sess.createQuery(criteria);
        List<Tutored> tutoreds =  q.getResultList();
        sess.close();
        return tutoreds;
    }
}
