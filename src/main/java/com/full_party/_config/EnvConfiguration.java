package com.full_party._config;

public class EnvConfiguration {

    private static final Boolean IS_DEPLOY_ENVIRONMENT = true;

    public static String getHost() {
        return IS_DEPLOY_ENVIRONMENT ? "fullpartyspring.com" : "localhost";
    }

    public static String getDomain() {
        return IS_DEPLOY_ENVIRONMENT ? ".fullpartyspring.com" : "localhost";
    }

    public static Integer getPort() {
        return IS_DEPLOY_ENVIRONMENT ? 443 : 3000;
    }
}
