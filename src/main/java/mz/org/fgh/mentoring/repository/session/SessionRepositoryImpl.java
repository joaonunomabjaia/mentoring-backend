package mz.org.fgh.mentoring.repository.session;

import io.micronaut.data.annotation.Repository;

import javax.persistence.EntityManager;

@Repository
public abstract class SessionRepositoryImpl implements SessionRepository {

    private final EntityManager entityManager;

    protected SessionRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
