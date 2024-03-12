package mz.org.fgh.mentoring.service.district;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.district.DistrictDTO;
import mz.org.fgh.mentoring.entity.location.District;
import mz.org.fgh.mentoring.repository.district.DistrictRepository;
import mz.org.fgh.mentoring.util.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Singleton
public class DistrictService {

    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public List<DistrictDTO> getAll(Long limit, Long offset) {
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
}
