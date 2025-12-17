package mz.org.fgh.mentoring.service.setting;

import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import mz.org.fgh.mentoring.dto.setting.SettingDTO;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.repository.settings.SettingsRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
@RequiredArgsConstructor
public class SettingService {

    private final SettingsRepository repo;

    /**
     * ✅ Cacheia a entidade inteira, porque precisamos de:
     * - enabled (controle de comportamento)
     * - value (config)
     * - type/description (para UI/admin, etc.)
     */
    @Cacheable("settings")
    protected Optional<Setting> getSettingCached(String key) {
        return repo.findByDesignationAndLifeCycleStatusNotEquals(key, LifeCycleStatus.DELETED);
    }

    // =====================================================
    // Enabled flags (controle de comportamento)
    // =====================================================
    public boolean isEnabled(String key, boolean def) {
        return getSettingCached(key)
                .map(Setting::getEnabled)
                .orElse(def);
    }

    // =====================================================
    // Value getters (NÃO filtram enabled)
    // =====================================================
    public Optional<String> getRawValue(String key) {
        return getSettingCached(key)
                .map(Setting::getValue)
                .map(v -> v == null ? null : v.trim())
                .filter(Utilities::stringHasValue);
    }

    public String get(String key, String def) {
        return getRawValue(key).orElse(def);
    }

    public boolean getBoolean(String key, boolean def) {
        return getRawValue(key)
                .map(Boolean::parseBoolean)
                .orElse(def);
    }

    public int getInt(String key, int def) {
        return getRawValue(key)
                .map(v -> {
                    try { return Integer.parseInt(v); }
                    catch (NumberFormatException e) { return def; }
                })
                .orElse(def);
    }

    public long getLong(String key, long def) {
        return getRawValue(key)
                .map(v -> {
                    try { return Long.parseLong(v); }
                    catch (NumberFormatException e) { return def; }
                })
                .orElse(def);
    }

    public double getDouble(String key, double def) {
        return getRawValue(key)
                .map(v -> {
                    try { return Double.parseDouble(v); }
                    catch (NumberFormatException e) { return def; }
                })
                .orElse(def);
    }

    public <E extends Enum<E>> E getEnum(String key, Class<E> type, E def) {
        return getRawValue(key)
                .map(v -> {
                    try { return Enum.valueOf(type, v.trim().toUpperCase(Locale.ROOT)); }
                    catch (IllegalArgumentException ex) { return def; }
                })
                .orElse(def);
    }

    // =====================================================
    // Helper: "apenas se enabled" (opcional, mas útil)
    // =====================================================
    public String getIfEnabled(String key, String def) {
        return getSettingCached(key)
                .filter(Setting::getEnabled)
                .map(Setting::getValue)
                .map(v -> v == null ? null : v.trim())
                .filter(Utilities::stringHasValue)
                .orElse(def);
    }

    public Optional<String> getRawValueIfEnabled(String key) {
        return getSettingCached(key)
                .filter(Setting::getEnabled)
                .map(Setting::getValue)
                .map(v -> v == null ? null : v.trim())
                .filter(Utilities::stringHasValue);
    }

    // =====================================================
    // Upsert (mantém o teu padrão)
    // =====================================================
    @CacheInvalidate(cacheNames = "settings", parameters = {"key"})
    public void upsert(String key, String value, String type, String description, boolean enabled, String actor) {
        var s = repo.findByDesignation(key).orElseGet(Setting::new);

        s.setDesignation(key);
        s.setValue(value);
        s.setType(type);
        s.setEnabled(enabled);
        s.setDescription(description);

        if (s.getId() == null) {
            s.setCreatedBy(actor != null ? actor : "system");
            s.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
            repo.save(s);
        } else {
            s.setUpdatedBy(actor != null ? actor : "system");
            repo.update(s);
        }
    }

    @CacheInvalidate(cacheNames = "settings", all = true)
    public void evictAll() {}

    public List<SettingDTO> findAll(Long limit, Long offset) {
        List<Setting> settings = (limit != null && offset != null && limit > 0)
                ? repo.findSettingWithLimit(limit, offset)
                : repo.findAll();

        return settings.stream()
                .map(SettingDTO::new)
                .collect(Collectors.toList());
    }
}
