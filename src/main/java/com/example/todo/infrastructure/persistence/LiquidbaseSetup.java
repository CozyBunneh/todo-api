package com.example.todo.infrastructure.persistence;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.common.CustomConfig;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.util.ExceptionUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.resource.SearchPathResourceAccessor;

@ApplicationScoped
public class LiquidbaseSetup {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquidbaseSetup.class);

    @ConfigProperty(name = "custom.liquibase.migrate")
    boolean runMigration;

    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    String datasourceUrl;

    @ConfigProperty(name = "quarkus.datasource.username")
    String datasourceUsername;

    @ConfigProperty(name = "quarkus.datasource.password")
    String datasourcePassword;

    @ConfigProperty(name = "quarkus.liquibase.change-log")
    String changeLogLocation;

    @ConfigProperty(name = "quarkus.liquibase.database-change-log-lock-table-name")
    String changeLogLockTableName;

    @ConfigProperty(name = "quarkus.liquibase.database-change-log-table-name")
    String changeLogTableName;

    @ConfigProperty(name = "quarkus.liquibase.default-schema-name")
    String defaultSchemaName;

    @ConfigProperty(name = "quarkus.liquibase.liquibase-schema-name")
    String liquibaseSchemaName;

    @Inject
    CustomConfig customConfig;

    public void runLiquibaseMigration(@Observes StartupEvent event) throws LiquibaseException {
        if (runMigration) {
            Liquibase liquibase = null;
            try {
                ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(
                        Thread.currentThread().getContextClassLoader());
                DatabaseConnection connection = DatabaseFactory.getInstance()
                        .openConnection(datasourceUrl, datasourceUsername, datasourcePassword, null, resourceAccessor);
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);

                database.setDatabaseChangeLogLockTableName(changeLogLockTableName);
                database.setDatabaseChangeLogTableName(changeLogTableName);
                database.setDefaultSchemaName(defaultSchemaName);
                database.setLiquibaseSchemaName(liquibaseSchemaName);

                liquibase = new Liquibase(changeLogLocation, resourceAccessor, database);
                liquibase.validate();
                liquibase.update(new Contexts(), new LabelExpression());
            } catch (Exception ex) {
                LOGGER.error("Liquibase Migration Exception Stack Trace: {}", ExceptionUtil.generateStackTrace(ex));
            } finally {
                if (liquibase != null) {
                    liquibase.close();
                }
            }
        }
    }
}