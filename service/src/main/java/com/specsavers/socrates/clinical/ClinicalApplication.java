package com.specsavers.socrates.clinical;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.specsavers.socrates.common", "com.specsavers.socrates.clinical"})
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class ClinicalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicalApplication.class, args);
    }

}
