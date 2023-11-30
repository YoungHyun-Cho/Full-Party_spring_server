package com.full_party.config;

import lombok.Getter;

public class EnvConfiguration {

    private static final Boolean IS_DEPLOY_ENVIRONMENT = true;

    public static Value getValues() {
        return new Value(IS_DEPLOY_ENVIRONMENT);
    }

    @Getter
    public static class Value {
        private String host;
        private Integer port;
        private String domain;

        public Value(Boolean isDeployEnv) {
            this.host = isDeployEnv ? "fullpartyspring.com" : "localhost";
            this.port = isDeployEnv ? 443 : 3000;
            this.domain = isDeployEnv ? ".fullpartyspring.com" : "localhost";
        }
    }
}
