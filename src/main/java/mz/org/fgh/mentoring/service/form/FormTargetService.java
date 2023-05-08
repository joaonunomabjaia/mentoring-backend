package mz.org.fgh.mentoring.service.form;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.formtarget.FormTarget;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.repository.tutor.FormTargetRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.List;

@Singleton
public class FormTargetService {

    private final FormTargetRepository formTargetRepository;
    public FormTargetService(FormTargetRepository formTargetRepository) {
        this.formTargetRepository = formTargetRepository;
    }

    public List<FormTarget> findFormTargetByTutor(final Tutor tutor){

        return this.formTargetRepository.findFormTargetByTutor(tutor.getUuid(), LifeCycleStatus.ACTIVE);
    }
}
