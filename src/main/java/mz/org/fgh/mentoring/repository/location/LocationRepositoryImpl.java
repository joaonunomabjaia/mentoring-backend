package mz.org.fgh.mentoring.repository.location;

import io.micronaut.data.annotation.Repository;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.Set;
import java.util.UUID;

@Repository
public abstract class LocationRepositoryImpl implements LocationRepository{

    @Override
    public void createOrUpdate(Set<Location> locations, User user) {
        for (Location location : locations) {
            Location l = findByUuid(location.getUuid());
            if (l != null) {
                location.setId(l.getId());
                location.setUpdatedAt(DateUtils.getCurrentDate());
                location.setUpdatedBy(user.getUuid());
                update(location);
            } else {
                location.setUuid(UUID.randomUUID().toString());
                location.setCreatedAt(DateUtils.getCurrentDate());
                location.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                location.setCreatedBy(user.getUuid());
                location.setLocationLevel("N/A");
                save(location);
            }

        }
    }
}
