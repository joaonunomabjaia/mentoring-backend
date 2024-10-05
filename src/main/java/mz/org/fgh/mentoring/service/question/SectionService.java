package mz.org.fgh.mentoring.service.question;


import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.question.Section;
import mz.org.fgh.mentoring.repository.question.SectionRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Singleton
public class SectionService {

    @Inject
    private SectionRepository sectionRepository;

    @Transactional
    public Section createSection(Section section) {
        return sectionRepository.save(section);
    }

    @Transactional
    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }

    public Optional<Section> getSection(Long id) {
        return sectionRepository.findById(id);
    }

    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    @Transactional
    public Section updateSection(Section section) {
        return sectionRepository.update(section);
    }

    public Optional<Section> getByUUID(String uuid) {
        return sectionRepository.findByUuid(uuid);
    }
}
