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
    public List<Tutor> search(String name, String nuit, User user, String phoneNumber) {
        // Construct the base SQL query
        String sql = "SELECT t.id " +
                "FROM tutors t INNER JOIN employee e ON t.EMPLOYEE_ID = e.ID " +
                "WHERE 1=1 ";

        // Add conditions based on provided parameters
        if (name != null) {
            sql += " AND (e.name LIKE '%" + name + "%' OR e.surname LIKE '%" + name + "%') ";
        }
        if (nuit != null) {
            sql += " AND e.NUIT LIKE '%" + nuit + "%' ";
        }
        if (phoneNumber != null) {
            sql += " AND e.PHONE_NUMBER LIKE '%" + phoneNumber + "%' ";
        }
        sql += addUserAuthCondition(user);

        // Execute the SQL query to get the list of IDs
        Query sqlQuery = this.session.getCurrentSession().createSQLQuery(sql);
        List<Long> ids = sqlQuery.getResultList();

        // Use CriteriaBuilder to create a query for fetching Tutor entities
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Tutor> criteria = builder.createQuery(Tutor.class);
        Root<Tutor> root = criteria.from(Tutor.class);
        criteria.select(root).where(root.get("id").in(ids));

        // Execute the CriteriaQuery to get the list of Tutor entities
        Query query = this.session.getCurrentSession().createQuery(criteria);
        List<Tutor> tutors = query.getResultList();

        return tutors;
    }

}
