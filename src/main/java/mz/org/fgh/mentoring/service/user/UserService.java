package mz.org.fgh.mentoring.service.user;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.entity.user.UserDTO;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.tutored.TutoredRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.tutored.TutoredService;

import java.util.Optional;

@Singleton
public class UserService {

    private final UserRepository userRepository;

    @Inject
    private TutorRepository tutorRepository;

    @Inject
    private TutoredRepository tutoredRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getByCredencials(User user) {
        Optional<User> possibleUser = userRepository.findByUsername(user.getUsername());

        if (possibleUser.isPresent()) {
            if (possibleUser.get().getPassword().equals(user.getPassword())) {
                /*if (possibleUser.get().isTutor()) {
                    possibleUser.get().setUserIndividual(tutorRepository.findByUser(possibleUser.get()));
                }*/
                return new UserDTO(possibleUser.get());
            }
        }

        return null;
    }
}
