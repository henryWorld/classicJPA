package com.specsavers.socrates.clinical.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 *OfsetDateTime is not one of the
 * supported Date type.
 * This configuration is to set up an OffsetDateTime provider
 * for auditing
 */
@Configuration
public class JpaAuditingConfig {
    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}
