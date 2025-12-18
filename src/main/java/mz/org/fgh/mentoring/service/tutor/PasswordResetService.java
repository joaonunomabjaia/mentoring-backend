package mz.org.fgh.mentoring.service.tutor;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.config.SettingKeys;
import mz.org.fgh.mentoring.dto.tutor.PasswordResetRequestDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.PasswordReset;
import mz.org.fgh.mentoring.repository.employee.EmployeeRepository;
import mz.org.fgh.mentoring.repository.settings.SettingsRepository;
import mz.org.fgh.mentoring.repository.tutor.PasswordResetRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.setting.SettingService;
import mz.org.fgh.mentoring.service.user.UserService;
import mz.org.fgh.mentoring.service.setting.SettingService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.util.EmailSender;
import java.security.SecureRandom;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static mz.org.fgh.mentoring.config.SettingKeys.PASSWORD_RESET_EXPIRATION_MINUTES;
import static mz.org.fgh.mentoring.config.SettingKeys.SERVER_BASE_URL;

@Singleton
public class PasswordResetService {

    @Inject
    private PasswordResetRepository passwordResetRepository;

    @Inject
    private TutorRepository tutorRepository;

    @Inject
    private EmployeeRepository employeeRepository;

    private final SettingService settings;
    private final UserRepository userRepository;

    @Inject
    private EmailSender emailSender;

    public PasswordResetService(SettingService settings, UserRepository userRepository) {
        this.settings = settings;
        this.userRepository = userRepository;
    }

    @Transactional
    public PasswordReset generateAndSendPasswordResetToken(PasswordResetRequestDTO dto) throws Exception {


        // 1. Validar se o email existe
        Optional<Employee> employeeOpt = employeeRepository.findByEmail(dto.getEmail());
        if (!employeeOpt.isPresent()) {
            throw new IllegalArgumentException("O email fornecido não está associado a nenhum Tutor.");
        }
        Employee employee = employeeOpt.get();

        // 2. Validar tutor
        Tutor tutor = tutorRepository.findByEmployee(employee);
        if (tutor == null) {
            throw new IllegalArgumentException("Nenhum tutor encontrado para este email.");
        }

        // 3. Apagar tokens antigos não usados e não expirados
        List<PasswordReset> oldTokens = passwordResetRepository.findByEmailAndUsedFalse(dto.getEmail());
        Date now = new Date();
        oldTokens.forEach(token -> {
            if (!token.isExpired()) {
                passwordResetRepository.delete(token);
            }
        });

        // 5. Gerar token conforme plataforma
        String token;
        if ("MOBILE".equalsIgnoreCase(dto.getChannel())) {
            token = generateNumericToken(6); // 6 dígitos para mobile
        } else {
            token = UUID.randomUUID().toString().replace("-", ""); // token longo para web
        }

        // 6. Criar entidade PasswordReset
        PasswordReset reset = new PasswordReset();
        reset.setEmail(dto.getEmail());
        reset.setToken(token);
        reset.setChannel(dto.getChannel());
        reset.setDeviceId(dto.getDeviceId());
        reset.setUsed(false);
        reset.setExpiresAt(DateUtils.addMinutesDate(DateUtils.getCurrentDate(), settings.getInt(PASSWORD_RESET_EXPIRATION_MINUTES, 15)));
        reset.setUuid(UUID.randomUUID().toString());
        reset.setCreatedAt(DateUtils.getCurrentDate());
        reset.setCreatedBy(userRepository.findByUsername("system").get().getUuid());
        reset.setLifeCycleStatus(LifeCycleStatus.ACTIVE);

        // 7. Salvar na BD
        passwordResetRepository.save(reset);

        // 8. Enviar email de recuperação
        emailSender.sendPasswordRecoveryEmail(
                employee,
                token,
                dto.getChannel().equalsIgnoreCase("WEB") ? "WEB" : "MOBILE",
                settings.get(SERVER_BASE_URL, "https://mentdev.csaude.org.mz"),
                settings.get(PASSWORD_RESET_EXPIRATION_MINUTES, "15") + " minutos"
        );

        return reset;
    }

    /**
     * Gera token numérico de N dígitos
     */
    private String generateNumericToken(int digits) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digits; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * Verifica se token é válido (não expirado e não usado)
     * Somente valida, não marca como usado
     */
    public boolean isTokenValid(String token) {
        Optional<PasswordReset> resetOpt = passwordResetRepository.findByToken(token);
        if (!resetOpt.isPresent()) return false;

        PasswordReset reset = resetOpt.get();
        Date now = new Date();
        return !reset.isUsed() && reset.getExpiresAt().after(now);
    }

    /**
     * Retorna Employee caso esteja liberarado para reset
     */
    public Employee validateTokenForReset(String token) {
        Optional<PasswordReset> resetOpt = passwordResetRepository.findByToken(token);
        if (!resetOpt.isPresent()) {
            throw new IllegalArgumentException("Token inválido.");
        }

        PasswordReset reset = resetOpt.get();
        Date now = new Date();

        if (reset.isUsed()) {
            throw new IllegalArgumentException("Token já foi utilizado.");
        }
        if (reset.getExpiresAt().before(now)) {
            throw new IllegalArgumentException("Token expirado.");
        }

        Optional<Employee> employeeOpt = employeeRepository.findByEmail(reset.getEmail());
        if (!employeeOpt.isPresent()) {
            throw new IllegalStateException("Funcionário não encontrado para este token.");
        }

        return employeeOpt.get();
    }

    /**
     * Consome o token e marca como usado
     * Chamado **somente após a senha ter sido alterada com sucesso**
     */
    @Transactional
    public void consumeTokenAfterReset(String token) {
        Optional<PasswordReset> resetOpt = passwordResetRepository.findByToken(token);
        if (!resetOpt.isPresent()) {
            throw new IllegalArgumentException("Token inválido.");
        }

        PasswordReset reset = resetOpt.get();
        reset.setUsed(true);
        reset.setUpdatedAt(new Date());
        passwordResetRepository.update(reset);
    }

    /**
     * Lista tokens expirados e não usados
     */
    public List<PasswordReset> findExpiredUnusedTokens() {
        return passwordResetRepository.findByUsedFalseAndExpiresAtBefore(new Date());
    }

    public List<PasswordReset> findByUsedFalseAndExpiresAtBefore(Date now) {
        return passwordResetRepository.findByUsedFalseAndExpiresAtBefore(now);
    }

    @Transactional
    public int deleteExpiredUnused(Date now) {
        if (now == null) now = DateUtils.getCurrentDate();

        long deleted = passwordResetRepository.deleteByUsedFalseAndExpiresAtBefore(now);
        return (int) deleted;
    }

}
