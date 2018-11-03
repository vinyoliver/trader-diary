package com.traderdiary.service;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class FlywayIntegrator implements Integrator {

    private static final String APP_DS = "java:jboss/datasources/traderDiaryDS";
    public static final Logger LOGGER = LoggerFactory.getLogger(FlywayIntegrator.class);

    @Override
    public void integrate(Configuration c, SessionFactoryImplementor sfi, SessionFactoryServiceRegistry sfsr) {

        LOGGER.info("Flyway: Initialization");

        final Flyway flyway = new Flyway();

        try {
            DataSource dataSource = InitialContext.doLookup(APP_DS);

            flyway.setDataSource(dataSource);
            if (flyway.info().current() == null) {
                LOGGER.info("Flyway: Create baseline when db already exists");
                flyway.setBaselineOnMigrate(true);
            }
            LOGGER.info("Flyway: Start migration");
            flyway.migrate();

        } catch (NamingException | FlywayException e) {
            LOGGER.error("Error while migrating:", e);
            throw new RuntimeException(e);
        }

        LOGGER.info("Flyway: Migration finished");
    }

    @Override
    public void integrate(MetadataImplementor mi, SessionFactoryImplementor sfi, SessionFactoryServiceRegistry sfsr) {
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sfi, SessionFactoryServiceRegistry sfsr) {
    }
}
