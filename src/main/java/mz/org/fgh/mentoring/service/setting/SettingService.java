package mz.org.fgh.mentoring.service.setting;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.setting.SettingDTO;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.partnersetting.PartnerSetting;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.settings.PartnerSettingRepository;
import mz.org.fgh.mentoring.repository.settings.SettingsRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class SettingService {

    private final SettingsRepository settingsRepository;
    private final PartnerSettingRepository partnerSettingRepository;
    private final TutorRepository tutorRepository;

    public SettingService(SettingsRepository settingsRepository,
                          PartnerSettingRepository partnerSettingRepository,
                          TutorRepository tutorRepository) {
        this.settingsRepository = settingsRepository;
        this.partnerSettingRepository = partnerSettingRepository;
        this.tutorRepository = tutorRepository;
    }

    public List<SettingDTO> findAll(Long limit, Long offset) {
        List<Setting> settings = (limit != null && offset != null && limit > 0)
                ? settingsRepository.findSettingWithLimit(limit, offset)
                : settingsRepository.findAll();

        return settings.stream()
                .map(SettingDTO::new)
                .collect(Collectors.toList());
    }

    public SettingDTO findSettingById(Long id) {
        return settingsRepository.findById(id)
                .map(SettingDTO::new)
                .orElseThrow(() -> new MentoringBusinessException("Setting not found for ID: " + id));
    }

    public List<SettingDTO> findSettingByTutor(String uuid) {
        try {
            Tutor tutor = tutorRepository.findByUuid(uuid)
                    .orElseThrow(() -> new MentoringBusinessException("Tutor not found for UUID: " + uuid));

            Partner partner = tutor.getEmployee().getPartner();
            List<PartnerSetting> partnerSettings = partnerSettingRepository.findByPartner(partner);

            if (partnerSettings.isEmpty()) return Collections.emptyList();

            return partnerSettings.stream()
                    .map(PartnerSetting::getSetting)
                    .map(SettingDTO::new)
                    .collect(Collectors.toList());

        } catch (NoResultException ex) {
            throw new MentoringBusinessException("Nenhum resultado encontrado!");
        }
    }

    public List<Setting> findSettingWithLimit(long limit, long offset) {
        return settingsRepository.findSettingWithLimit(limit, offset);
    }

    public Optional<Setting> getSettingByDeignation(String code) {
        return settingsRepository.findByDesignation(code);
    }
}
