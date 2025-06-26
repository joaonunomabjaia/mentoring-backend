package mz.org.fgh.mentoring.service.location;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.province.ProvinceDTO;
import mz.org.fgh.mentoring.entity.location.Province;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.province.ProvinceRepository;
import mz.org.fgh.mentoring.util.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

/**
 * @author Jose Julai Ritsure
 */
@Singleton
public class ProviceService {

    private final ProvinceRepository provinceRepository;
    public ProviceService(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    public Province createProvince(Province province) {
        return this.provinceRepository.save(province);
    }

    public Province findProvinceById(final Long id) {
        Optional<Province> optProvince = this.provinceRepository.findById(id);
        if(optProvince == null){
            throw new MentoringBusinessException("Province with ID: "+id+" was not found.");
        }
        return optProvince.get();
    }

    public Province findProvinceByDesignation(final String designation){
        List<Province> provinces = this.provinceRepository.findByDesignation(designation);
        if(provinces.isEmpty()){
            throw new MentoringBusinessException("Province : "+designation+" was not found.");
        }
        return provinces.get(0);
    }

    public List<ProvinceDTO> findAllProvinces() {
        try {
            return Utilities.parseList(this.provinceRepository.findAll(), ProvinceDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProvinceDTO> getAll(Long limit, Long offset) {
        try {
            return Utilities.parseList(this.provinceRepository.findAll(), ProvinceDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    public ProvinceDTO getById(Long id){

        Province province = this.provinceRepository.findById(id).get();

        return new ProvinceDTO(province);
    }

    public Page<Province> findAll(@Nullable Pageable pageable) {
        return provinceRepository.findAll(pageable);
    }

    public Page<Province> searchByName(String name, Pageable pageable) {
        return provinceRepository.findByDesignationIlike("%" + name + "%", pageable);
    }
}
