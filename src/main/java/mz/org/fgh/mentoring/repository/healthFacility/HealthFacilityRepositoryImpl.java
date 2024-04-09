package mz.org.fgh.mentoring.repository.healthFacility;

import io.micronaut.data.annotation.Repository;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public abstract class HealthFacilityRepositoryImpl implements HealthFacilityRepository{

    private final SessionFactory session;

    protected HealthFacilityRepositoryImpl(SessionFactory session) {
        this.session = session;
    }

    @Override
    public List<HealthFacility> getAllOfDistrict(List<String> uuids) {
        EntityManager entityManager = this.session.createEntityManager();

        String sql =    "SELECT  hf " +
                        "FROM HealthFacility hf INNER JOIN hf.district d " +
                        "WHERE hf.lifeCycleStatus = :lifeCycleStatus and d.uuid in (:uuidList) " +
                        "ORDER BY d.description ";

        return  entityManager.createQuery(sql, HealthFacility.class)
                .setParameter("lifeCycleStatus", LifeCycleStatus.ACTIVE)
                .setParameter("uuidList", uuids)
                .getResultList();
    }
}
