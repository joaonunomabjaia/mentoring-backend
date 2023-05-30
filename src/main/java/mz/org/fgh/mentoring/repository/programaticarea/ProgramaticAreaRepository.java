package mz.org.fgh.mentoring.repository.programaticarea;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.programaticarea.ProgramaticArea;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.List;

@Repository
public interface ProgramaticAreaRepository extends CrudRepository<ProgramaticArea, Long> {

    @Override
    List<ProgramaticArea> findAll();

    @Query("select p from ProgramaticArea p where p.code like concat(concat('%', :code) ,'%')  and p.name like concat(concat('%', :name),'%') and p.lifeCycleStatus =:lifeCycleStatus")
    public List<ProgramaticArea> findBySelectedFilter(final String code, final String name, final LifeCycleStatus lifeCycleStatus);
}
