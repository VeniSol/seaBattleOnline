package com.example.seabattle;

import com.example.seabattle.Services.SeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StarterGame implements CommandLineRunner {
    @Autowired
    SeaService seaService;
    @Override
    public void run(String... args) {
        seaService.startGame();
    }
}