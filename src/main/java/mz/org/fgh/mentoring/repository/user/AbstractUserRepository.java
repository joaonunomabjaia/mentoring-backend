package mz.org.fgh.mentoring.repository.user;

import io.micronaut.data.annotation.Repository;
import mz.org.fgh.mentoring.base.AbstaractBaseRepository;
import mz.org.fgh.mentoring.entity.user.User;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public abstract class AbstractUserRepository extends AbstaractBaseRepository implements UserRepository {

    private final SessionFactory session;
    public AbstractUserRepository(SessionFactory session) {
        this.session = session;
    }


    @Transactional
    @Override
    public List<User> search(User user, String name, Long nuit, String userName) {

        String sql = "SELECT u.id " + "FROM users u INNER JOIN employee e ON u.EMPLOYEE_ID = e.ID " +
                "where 1=1";
                if(name != null) {
                sql += " AND e.name like '%" + name + "%'  ";
                }
                if(nuit != null) {
                    sql += " AND e.nuit like '%" + nuit + "%' ";
                }
                if(userName != null){
                    sql += " AND u.username like '%" + userName + "%' ";
                }
                    sql += addUserAuthCondition(user);

        Query query = this.session.getCurrentSession().createSQLQuery(sql);

        List<Long> ids = query.getResultList();

        CriteriaBuilder bulder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = bulder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(root.get("id").in(ids));
        Query q = this.session.getCurrentSession().createQuery(criteriaQuery);

        List<User> users = q.getResultList();

        return users;
    }
}
