package mz.org.fgh.mentoring.service.form;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.form.FormSection;
import mz.org.fgh.mentoring.repository.form.FormSectionRepository;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Singleton
public class FormSectionService {

    @Inject
    private FormSectionRepository formSectionRepository;

    @Transactional
    public FormSection createFormSection(FormSection formSection) {
        return formSectionRepository.save(formSection);
    }

    @Transactional
    public void deleteFormSection(Long id) {
        formSectionRepository.deleteById(id);
    }

    public Optional<FormSection> getFormSection(Long id) {
        return formSectionRepository.findById(id);
    }

    public List<FormSection> getAllFormSections() {
        return formSectionRepository.findAll();
    }

    public Page<FormSection> getAllFormSectionsPaged(Pageable pageable) {
        return formSectionRepository.findAll(pageable);
    }

    @Transactional
    public FormSection updateFormSection(FormSection formSection) {
        return formSectionRepository.update(formSection);
    }

    public Optional<FormSection> getByUUID(String uuid) {
        return formSectionRepository.findByUuid(uuid);
    }

    public List<FormSection> getByForm(Form form) {
        return formSectionRepository.findByForm(form);
    }
}
