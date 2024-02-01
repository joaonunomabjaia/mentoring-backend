package mz.org.fgh.mentoring.service.province;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.professionalCategory.ProfessionalCategoryDTO;
import mz.org.fgh.mentoring.dto.province.ProvinceDTO;
import mz.org.fgh.mentoring.repository.province.ProvinceRepository;
import mz.org.fgh.mentoring.util.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Singleton
public class ProvinceServce {

    private final ProvinceRepository provinceRepository;

    public ProvinceServce(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    public List<ProvinceDTO> getAll(Long limit, Long offset) {
        try {
            return Utilities.parseList(this.provinceRepository.findAll(), ProvinceDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
