package mz.org.fgh.mentoring.base;

import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.location.District;
import mz.org.fgh.mentoring.entity.location.Province;
import mz.org.fgh.mentoring.entity.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AbstaractBaseRepository {

    protected String addUserAuthCondition(User user) {
        String sql = "";

        if (user.hasNationalAccess()) {
            return "";
        } else if (user.hasProvincialAccess()) {
            List<Province> grantedProvinces = user.getGrantedProvinces();
            List<Long> parentList = new ArrayList<>();

            for (Province p: grantedProvinces) {
                parentList.add(p.getId());
            }

            sql +=  " and (exists(select l.ID from location l where l.PROVINCE_ID IN ("+String.join(",", parentList.stream().map(n -> ("'" + n + "'")).collect(Collectors.toList()))+") AND l.EMPLOYEE_ID = e.ID) " +
                    "       or exists(select l.ID  " +
                    "               from location l inner join districts d on d.ID = l.DISTRICT_ID " +
                    "                           inner join provinces p on p.ID = d.PROVINCE_ID " +
                    "             where p.ID in ("+String.join(",", parentList.stream().map(n -> ("'" + n + "'")).collect(Collectors.toList()))+") AND l.EMPLOYEE_ID = e.ID) " +
                    "       or exists(select l.ID  " +
                    "               from location l inner JOIN health_facilities hf on hf.ID = l.HEALTH_FACILITY_ID " +
                    "                           inner join districts d on d.ID = hf.DISTRICT_ID " +
                    "                           inner join provinces p on p.ID = d.PROVINCE_ID " +
                    "                            " +
                    "             where p.ID in ("+String.join(",", parentList.stream().map(n -> ("'" + n + "'")).collect(Collectors.toList()))+") AND l.EMPLOYEE_ID = e.ID)) ";


        } else if (user.hasDistrictAccess()) {

            List<District> grantedLocations = user.getGrantedDistricts();
            List<Long> parentList = new ArrayList<>();

            for (District p: grantedLocations) {
                parentList.add(p.getId());
            }

            sql += " and exists(select l.ID from location l where l.DISTRICT_ID IN ("+String.join(",", parentList.stream().map(n -> ("'" + n + "'"))
                    .collect(Collectors.toList()))+") AND l.EMPLOYEE_ID = e.ID) ";


        } else if (user.hasHFAccess()) {
            List<HealthFacility> grantedLocations = user.getGrantedHFs();
            List<Long> parentList = new ArrayList<>();

            for (HealthFacility p: grantedLocations) {
                parentList.add(p.getId());
            }

            sql += " and exists(select l.ID from location l where l.HEALTH_FACILITY_ID IN ("+String.join(",", parentList.stream().map(n -> ("'" + n + "'"))
                    .collect(Collectors.toList()))+") AND l.EMPLOYEE_ID = e.ID) ";

        }
        return sql;
    }

}
