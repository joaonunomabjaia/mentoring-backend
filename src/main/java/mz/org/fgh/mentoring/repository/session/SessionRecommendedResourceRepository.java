package mz.org.fgh.mentoring.repository.session;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.session.SessionRecommendedResource;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRecommendedResourceRepository extends CrudRepository<SessionRecommendedResource, Long> {

    Optional<SessionRecommendedResource> findBySessionId(Long sessionId);

    Optional<SessionRecommendedResource> findByTutored(Tutored tutored);

    Optional<SessionRecommendedResource> findByTutor(Tutor tutor);

    List<SessionRecommendedResource> findAllByTutored(Tutored tutored);

    List<SessionRecommendedResource> findAllByNotificationStatus(SessionRecommendedResource.NotificationStatus notificationStatus);

    SessionRecommendedResource findByTutoredIdAndResourceLink(Long tutoredId, String resourceLink);

    Optional<SessionRecommendedResource> findByUuid(String uuid);
    Optional<SessionRecommendedResource> findByTutoredUuidAndResourceLink(String tutoredUuid, String resourceLink);
    List<SessionRecommendedResource> findAllByTutoredUuid(String tutoredUuid);

    List<SessionRecommendedResource> findByNotificationStatus(SessionRecommendedResource.NotificationStatus status);

    Optional<SessionRecommendedResource> findByToken(String token);
}
