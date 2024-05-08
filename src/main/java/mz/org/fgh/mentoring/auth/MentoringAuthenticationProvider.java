package mz.org.fgh.mentoring.auth;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.role.UserRole;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.role.RoleAuthorityRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.Utilities;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class MentoringAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOG = LoggerFactory.getLogger(MentoringAuthenticationProvider.class);

    final UserRepository users;
    final RoleAuthorityRepository roleAuthorityRepository;

    public MentoringAuthenticationProvider(UserRepository users, RoleAuthorityRepository roleAuthorityRepository) {
        this.users = users;
        this.roleAuthorityRepository = roleAuthorityRepository;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        final String identity = (String) authenticationRequest.getIdentity();

        LOG.debug("User {} tries to login...", identity);

        return Flowable.create(emitter -> {
            Optional<User> possibleUser = users.findByUsername(identity);

            if (possibleUser.isPresent()) {


                String secret = (String) authenticationRequest.getSecret();

                if (Utilities.MD5Crypt(possibleUser.get().getSalt()+":"+secret).equals(possibleUser.get().getPassword())) {
                    LOG.debug("User {} logged in...", identity);
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("userInfo", possibleUser.get().getId());
                    userMap.put("useruuid", possibleUser.get().getUuid());

                    emitter.onNext(AuthenticationResponse.success((String) identity, getAutorities(possibleUser.get()), userMap));
                    emitter.onComplete();
                    return;
                } else {
                    LOG.debug("Wrong password for user {} ...", identity);
                }
            } else {
                LOG.debug("No user {} found in the system...", identity);
            }
            emitter.onError(new AuthenticationException(new AuthenticationFailed("Utilizador ou senha inválida!")));
        }, BackpressureStrategy.ERROR);
    }

    private Collection<String> getAutorities(User user) {

        Collection<String> autorities = new ArrayList<>();

        for (UserRole userRole : user.getUserRoles()) {
            autorities.add(userRole.getRole().getCode());
        }
        return autorities;
    }
}
