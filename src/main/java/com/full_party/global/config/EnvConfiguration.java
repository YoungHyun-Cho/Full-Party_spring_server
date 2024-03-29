package com.full_party.global.config;

public class EnvConfiguration {

    private static final Boolean IS_DEPLOY_ENVIRONMENT = false;

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
