package mz.org.fgh.mentoring.base;

import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

public abstract class BaseService {
    protected void addCreationAuditInfo(BaseEntity entity, User user) {
        entity.setCreatedBy(user.getUuid());
        entity.setCreatedAt(DateUtils.getCurrentDate());
        entity.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
    }

    protected void addUpdateAuditInfo(BaseEntity newEntity, BaseEntity oldEntity, User user) {
        newEntity.setCreatedBy(oldEntity.getCreatedBy());
        newEntity.setCreatedAt(oldEntity.getCreatedAt());
        newEntity.setLifeCycleStatus(oldEntity.getLifeCycleStatus());
        newEntity.setUpdatedBy(user.getUuid());
        newEntity.setUpdatedAt(DateUtils.getCurrentDate());
    }
}
