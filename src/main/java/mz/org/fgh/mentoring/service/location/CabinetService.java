package mz.org.fgh.mentoring.service.location;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.location.CabinetDTO;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.error.RecordInUseException;
import mz.org.fgh.mentoring.repository.location.CabinetRepository;
import mz.org.fgh.mentoring.repository.mentorship.MentorshipRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class CabinetService {

    private final CabinetRepository cabinetRepository;
    // Se for usar verificação de vínculos no delete, injete o repositório apropriado:
     private final MentorshipRepository mentorshipRepository;

    public CabinetService(CabinetRepository cabinetRepository, MentorshipRepository mentorshipRepository) {
        this.cabinetRepository = cabinetRepository;
        this.mentorshipRepository = mentorshipRepository;
    }

    /* =========================
       NOVO PADRÃO (paginação/busca/CRUD por UUID)
       ========================= */

    public Page<Cabinet> findAll(@Nullable Pageable pageable) {
        return cabinetRepository.findAll(pageable);
    }

    public Page<Cabinet> searchByName(String name, Pageable pageable) {
        return cabinetRepository.findByNameIlike("%" + name + "%", pageable);
    }

    @Transactional
    public Cabinet create(Cabinet cabinet) {
        cabinet.setUuid(java.util.UUID.randomUUID().toString());
        cabinet.setCreatedAt(mz.org.fgh.mentoring.util.DateUtils.getCurrentDate());
        cabinet.setLifeCycleStatus(mz.org.fgh.mentoring.util.LifeCycleStatus.ACTIVE);
        return cabinetRepository.save(cabinet);
    }

    @Transactional
    public Cabinet update(Cabinet cabinet) {
        Cabinet existing = cabinetRepository.findByUuid(cabinet.getUuid())
                .orElseThrow(() -> new MentoringBusinessException("Gabinete com UUID " + cabinet.getUuid() + " não encontrado."));

        // Ajuste os campos que podem ser atualizados
        existing.setName(cabinet.getName());
        existing.setUpdatedBy(cabinet.getUpdatedBy());
        existing.setUpdatedAt(mz.org.fgh.mentoring.util.DateUtils.getCurrentDate());

        return cabinetRepository.update(existing);
    }

    @Transactional
    public Cabinet updateLifeCycleStatus(String uuid,
                                         mz.org.fgh.mentoring.util.LifeCycleStatus status,
                                         String userUuid) {
        Cabinet cabinet = cabinetRepository.findByUuid(uuid)
                .orElseThrow(() -> new MentoringBusinessException("Gabinete com UUID " + uuid + " não encontrado."));

        cabinet.setLifeCycleStatus(status);
        cabinet.setUpdatedBy(userUuid);
        cabinet.setUpdatedAt(mz.org.fgh.mentoring.util.DateUtils.getCurrentDate());

        return cabinetRepository.update(cabinet);
    }

    @Transactional
    public void delete(String uuid) {
        Cabinet cabinet = cabinetRepository.findByUuid(uuid)
                .orElseThrow(() -> new MentoringBusinessException("Gabinete com UUID " + uuid + " não encontrado."));

        // Exemplo de verificação futura (mantenha comentado até ter o repositório correto):
         long count = mentorshipRepository.countByCabinet(cabinet);
         if (count > 0) {
             throw new RecordInUseException("Sector está associado a sessoes.");
         }

        cabinetRepository.delete(cabinet);
    }

    /* =========================
       MÉTODOS LEGADOS (mantidos se ainda usar o endpoint anônimo /getall)
       ========================= */

    public List<Cabinet> findAll() {
        return cabinetRepository.findAll();
    }

    public List<CabinetDTO> findAllCabinets(Long limit, Long offset) {
        List<Cabinet> cabinets;

        if (limit != null && offset != null && limit > 0) {
            cabinets = this.findCabinetWithLimit(limit, offset);
        } else {
            cabinets = cabinetRepository.findAll();
        }

        List<CabinetDTO> cabinetDTOS = new ArrayList<>(cabinets.size());
        for (Cabinet cabinet : cabinets) {
            cabinetDTOS.add(new CabinetDTO(cabinet));
        }
        return cabinetDTOS;
    }

    public Cabinet findCabinetById(Long id) {
        Optional<Cabinet> optCabinet = cabinetRepository.findById(id);
        if (optCabinet.isEmpty()) {
            throw new MentoringBusinessException("Cabinet with ID - " + id + " does not exist.");
        }
        return optCabinet.get();
    }

    public Cabinet createCabinet(Cabinet cabinet) {
        return cabinetRepository.save(cabinet);
    }

    public List<Cabinet> findCabinetWithLimit(long limit, long offset){
        return this.cabinetRepository.findCabinetWithLimit(limit, offset);
    }
}
