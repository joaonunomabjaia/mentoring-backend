package mz.org.fgh.mentoring.service.session;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.session.SessionStatus;
import mz.org.fgh.mentoring.repository.session.SessionStatusRepository;

import java.util.List;
import java.util.Optional;

@Singleton
public class SessionStatusService {

    private final SessionStatusRepository sessionStatusRepository;

    public SessionStatusService(SessionStatusRepository sessionStatusRepository) {
        this.sessionStatusRepository = sessionStatusRepository;
    }

    public List<SessionStatus> findAll(){
        return this.sessionStatusRepository.findAll();
    }

    public Optional<SessionStatus> findById(final Long id){
        return this.sessionStatusRepository.findById(id);
    }

    public Optional<SessionStatus> findByUuid(final String uuid){
      return this.sessionStatusRepository.findByUuid(uuid);
    }

    public SessionStatus findByCode(final String code){
        return this.sessionStatusRepository.findByCode(code);
    }

}
