package com.specsavers.socrates.clinical.tools;

import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClassImportIntegratorProvider
        implements IntegratorProvider {

    @Override
    public List<Integrator> getIntegrators() {
        return List.of(RootAwareEventListenerIntegrator.INSTANCE);
    }
}
