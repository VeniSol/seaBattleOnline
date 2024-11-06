package com.example.seabattle.Controllers;

import com.example.seabattle.Model.Fields;
import com.example.seabattle.Model.Moves;
import com.example.seabattle.Model.Player;
import com.example.seabattle.Repositories.PlayerRepository;
import com.example.seabattle.RequestBody.MoveBody;
import com.example.seabattle.Services.SeaService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;

@RestController
public class Controller {
    @Autowired
    private SeaService seaService;
    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/get-fields")
    public Fields home(HttpServletRequest request) {
        return seaService.getFields(getIdByCookie(request));
    }

    @PostMapping("/new-move")
    public Fields newMove(@RequestBody MoveBody move,HttpServletRequest request) {
        int playerId = getIdByCookie(request);
        Player player = playerRepository.findByPlayerId(playerId);
        seaService.savingMovesLocation(move.getX(), move.getY(), player);
        return seaService.getFields(playerId);
    }

    private Integer getIdByCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return -1;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Haruka"))
                return Integer.valueOf(cookie.getValue());
        }
        return -1;
    }

    @GetMapping("/who-win")
    public int checkWin(){
        return seaService.getWhoWin();
    }

    @GetMapping("/get-current-turn")
    public Player getPlayerCurrentTurn(){
        return seaService.getCurrentPlayer();
    }
}
