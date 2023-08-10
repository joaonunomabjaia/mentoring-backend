package mz.org.fgh.mentoring.service.setting;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.setting.SettingDTO;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.partnersetting.PartnerSetting;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.partner.PartnerRepository;
import mz.org.fgh.mentoring.repository.settings.PartnerSettingRepository;
import mz.org.fgh.mentoring.repository.settings.SettingsRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.service.tutor.TutorService;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class SettingService {

    private final SettingsRepository settingsRepository;

    private final PartnerSettingRepository partnerSettingRepository;

    private final TutorRepository tutorRepository;

    public SettingService(SettingsRepository settingsRepository, PartnerSettingRepository partnerSettingRepository, TutorRepository tutorRepository) {
        this.settingsRepository = settingsRepository;
        this.partnerSettingRepository = partnerSettingRepository;
        this.tutorRepository = tutorRepository;
    }

    public List<SettingDTO> findAll() {
        List<Setting> settings = this.settingsRepository.findAll();
        List<SettingDTO> settingDTOS = new ArrayList<SettingDTO>(settings.size());
        for (Setting setting: settings) {
            settingDTOS.add(new SettingDTO(setting));
        }
        return settingDTOS;
    }

    public SettingDTO findSettingById(final Long id){
        Setting setting = this.settingsRepository.findById(id).get();
        SettingDTO settingDTO = new SettingDTO(setting);
        return settingDTO;
    }

    public List<SettingDTO> findSettingByTutor(final String uuid){

        List<Setting> settings = new ArrayList<Setting>(0);

        try {
            final Tutor tutor = tutorRepository.findByUuid(uuid).get();

            final Partner partner = tutor.getPartner();

            List<PartnerSetting> partnerSettings = this.partnerSettingRepository.findByPartner(partner);
            if(partnerSettings.isEmpty()) {
                return new ArrayList<>(0);
            }
            for (PartnerSetting partnerSetting : partnerSettings) {
                Setting setting = partnerSetting.getSetting();
                settings.add(setting);
            }
        } catch (final NoResultException ex) {
            throw new MentoringBusinessException("Nenhum resultado encontrado!");
        }

        List<SettingDTO> settingDTOS = new ArrayList<SettingDTO>(settings.size());
        for (Setting setting: settings) {
            settingDTOS.add(new SettingDTO(setting));
        }
        return settingDTOS;
    }
}
