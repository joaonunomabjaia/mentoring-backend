package mz.org.fgh.mentoring.service.question;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.form.ResponseTypeDTO;
import mz.org.fgh.mentoring.entity.question.ResponseType;
import mz.org.fgh.mentoring.repository.question.ResponseTypeRepository;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ResponseTypeService {

    @Inject
    ResponseTypeRepository responseTypeRepository;

    public List<ResponseTypeDTO> findAll(){
        List<ResponseTypeDTO> responseTypeDTOS = new ArrayList<>();
        List<ResponseType> responseTypes = this.responseTypeRepository.findAll();
        for (ResponseType responseType :responseTypes) {
            ResponseTypeDTO responseTypeDTO = new ResponseTypeDTO(responseType);
            responseTypeDTOS.add(responseTypeDTO);
        }
        return responseTypeDTOS;
    }

    public ResponseTypeDTO getByCode(String code) {
        ResponseType responseType = responseTypeRepository.getByCode(code);
        ResponseTypeDTO responseTypeDTO = new ResponseTypeDTO(responseType);
        return responseTypeDTO;
    }
}
