package com.full_party;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FullPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FullPartyApplication.class, args);
    }
}
