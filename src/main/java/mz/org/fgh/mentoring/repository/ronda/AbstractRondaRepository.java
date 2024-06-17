package mz.org.fgh.mentoring.repository.ronda;

import io.micronaut.data.annotation.Repository;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.base.AbstaractBaseRepository;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

@Repository
public abstract class AbstractRondaRepository extends AbstaractBaseRepository implements RondaRepository {

    private final SessionFactory session;

    @Inject
    private RondaMentorRepository rondaMentorRepository;

    @Inject
    private RondaMenteeRepository rondaMenteeRepository;


    protected AbstractRondaRepository(SessionFactory session) {
        this.session = session;
    }

    @Override
    public List<Ronda> getAllOfMentor(String mentorUuid) {
        String sql = "SELECT DISTINCT r.id " +
                    "FROM rondas r " +
                    "INNER JOIN ronda_mentor rmr ON rmr.RONDA_ID = r.id " +
                    "INNER JOIN tutors t ON rmr.MENTOR_ID = t.id " +
                    "WHERE t.uuid = :mentorUuid";

        Query qw = this.session.getCurrentSession().createSQLQuery(sql);
        qw.setParameter("mentorUuid", mentorUuid);
        List<Long> ids = qw.getResultList();

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Ronda> criteria = builder.createQuery(Ronda.class);
        Root<Ronda> root = criteria.from(Ronda.class);
        root.fetch("rondaMentors", JoinType.LEFT); // Eagerly fetch RondaMentors
        root.fetch("rondaMentees", JoinType.LEFT); // Eagerly fetch RondaMentees
        criteria.select(root).where(root.get("id").in(ids));

        Query q = this.session.getCurrentSession().createQuery(criteria);
        List<Ronda> rondas = q.getResultList();

        return rondas;
    }

}
