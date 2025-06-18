package mz.org.fgh.mentoring.service.district;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.district.DistrictDTO;
import mz.org.fgh.mentoring.entity.location.District;
import mz.org.fgh.mentoring.repository.district.DistrictRepository;
import mz.org.fgh.mentoring.util.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DistrictService {

    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public List<DistrictDTO> getAll(Long limit, Long offset) {
        try {
            List<District> districts = new ArrayList<>();
            if(limit!=null && offset!=null && limit>0) {
               districts = districtRepository.findDistrictsWithLimit(limit, offset);
            } else {
               districts = districtRepository.findAll();
            }
            return Utilities.parseList(districts, DistrictDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    public List<DistrictDTO> getAll() {
        try {
            return Utilities.parseList(this.districtRepository.findAll(), DistrictDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    public List<DistrictDTO> getAllOfProvince(Long provinceId) {
        try {
            return Utilities.parseList(this.districtRepository.findByProvinceId(provinceId), DistrictDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    public DistrictDTO getById(Long id){

        District district = this.districtRepository.findById(id).get();

        return new DistrictDTO(district);
    }
    public District getById_1(Long id){

        District district = this.districtRepository.findById(id).get();

        return district;
    }

    public Page<District> findAll(@Nullable Pageable pageable) {
        return districtRepository.findAll(pageable);
    }

    public Page<District> searchByName(String name, Pageable pageable) {
        return districtRepository.findByDescriptionIlike("%" + name + "%", pageable);
    }
}
