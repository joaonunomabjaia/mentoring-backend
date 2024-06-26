package mz.org.fgh.mentoring.repository.ronda;

import io.micronaut.data.annotation.Repository;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.base.AbstaractBaseRepository;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.LongType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
        List<Long> ids;
        List<Ronda> rondas;
        try (Session session = this.session.openSession()) {
            Transaction transaction = session.beginTransaction();

            String sql = "SELECT DISTINCT r.id " +
                    "FROM rondas r " +
                    "INNER JOIN ronda_mentor rmr ON rmr.RONDA_ID = r.id " +
                    "INNER JOIN tutors t ON rmr.MENTOR_ID = t.id " +
                    "WHERE t.uuid = :mentorUuid";

            NativeQuery<Long> qw = session.createSQLQuery(sql);
            qw.setParameter("mentorUuid", mentorUuid);
            qw.addScalar("id", LongType.INSTANCE);
            ids = qw.list();

            if (ids.isEmpty()) {
                transaction.commit();
                return Collections.emptyList();
            }

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ronda> criteria = builder.createQuery(Ronda.class);
            Root<Ronda> root = criteria.from(Ronda.class);
            criteria.select(root).where(root.get("id").in(ids));

            org.hibernate.query.Query<Ronda> q = session.createQuery(criteria);
            rondas = q.getResultList();

            // Fetch the collections in separate queries
            for (Ronda ronda : rondas) {
                Hibernate.initialize(ronda.getRondaMentors());
                Hibernate.initialize(ronda.getRondaMentees());
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch Rondas for mentor: " + mentorUuid, e);
        }
        return rondas;
    }


}
