package com.example.seabattle;

import com.example.seabattle.Repositories.ShipsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SeaBattleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeaBattleApplication.class, args);

    }
}
