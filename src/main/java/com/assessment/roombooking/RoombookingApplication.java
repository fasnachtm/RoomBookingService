package com.assessment.roombooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories({"com.assessment.roombooking.repositories"})
public class RoombookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoombookingApplication.class, args);
    }

}
