package com.example.common;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "custom")
public interface CustomConfig {

    Db db();

    Liquibase liquibase();

    interface Db {
        String name();

        String username();

        String password();

        String url();

        Integer port();
    }

    interface Liquibase {

        Boolean migrate();

        Optional<List<String>> searchPaths();
    }
}