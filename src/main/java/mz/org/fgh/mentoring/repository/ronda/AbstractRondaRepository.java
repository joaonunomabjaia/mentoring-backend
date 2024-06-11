package mz.org.fgh.mentoring.repository.ronda;

import io.micronaut.data.annotation.Repository;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.base.AbstaractBaseRepository;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaMentee;
import mz.org.fgh.mentoring.entity.ronda.RondaMentor;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
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

    @Transactional
    @Override
    public List<Ronda> getAllOfMentor(String mentorUuid) {
        String sql = " SELECT DISTINCT(r.id) " +
                     " FROM rondas r " +
                     " INNER JOIN ronda_mentor rmr ON rmr.RONDA_ID = r.id " +
                     " INNER JOIN tutors t ON rmr.MENTOR_ID = t.id " +
                     " INNER JOIN ronda_mentee rme ON rme.RONDA_ID = r.id " +
                     " where 1=1 ";

        if (mentorUuid != null) {
            sql += " AND t.uuid like '" + mentorUuid + "'";
        }

        Query qw = this.session.getCurrentSession().createSQLQuery(sql);
        List<Long> ids = qw.getResultList();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Ronda> criteria = builder.createQuery(Ronda.class);
        Root<Ronda> root = criteria.from(Ronda.class);
        criteria.select(root).where(root.get("id").in(ids));
        Query q = this.session.getCurrentSession().createQuery(criteria);
        List<Ronda> rondas = q.getResultList();

        for (Ronda ronda: rondas) {
            List<RondaMentor> rondaMentors = rondaMentorRepository.findByRonda(ronda.getId(), LifeCycleStatus.ACTIVE);
            ronda.setRondaMentors(rondaMentors);
            List<RondaMentee> rondaMentees = rondaMenteeRepository.findByRonda(ronda.getId(), LifeCycleStatus.ACTIVE);
            ronda.setRondaMentees(rondaMentees);
        }

        return rondas;
    }
}
