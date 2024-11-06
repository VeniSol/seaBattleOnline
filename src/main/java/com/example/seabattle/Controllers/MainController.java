package com.example.seabattle.Controllers;

import com.example.seabattle.Model.Player;
import com.example.seabattle.Repositories.PlayerRepository;
import com.example.seabattle.Services.SeaService;
import com.sun.jdi.IntegerValue;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class MainController {
    private final SeaService seaService;
    private final PlayerRepository playerRepository;

    public MainController(SeaService seaService, PlayerRepository playerRepository) {
        this.seaService = seaService;
        this.playerRepository = playerRepository;
    }

    @GetMapping("/")
    @Modifying
    public String go(HttpServletResponse response, HttpServletRequest request) throws IOException {
        if (!isCookieAlreadySet(request)) {
            if (playerRepository.findByPlayerId(1).getIsActive() && playerRepository.findByPlayerId(2).getIsActive()) {
                return "redirect:/error";
            } else {
                int idPlayer = playerRepository.findByPlayerId(1).getIsActive() ? 2 : 1;
                setCookie(response, idPlayer);
                Player player = playerRepository.findByPlayerId(idPlayer);
                player.setIsActive(true);
                playerRepository.save(player);
            }
        }
        return "main";
    }

    private void setCookie(HttpServletResponse response, int idPlayer) {
        Cookie cookie = new Cookie("Haruka", String.valueOf(idPlayer));
        response.addCookie(cookie);
    }

    private boolean isCookieAlreadySet(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return false;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Haruka")) return true;
        }
        return false;
    }

    @GetMapping("/info")
    public String endWindow(HttpServletRequest request, Model model) {
        if (getIdByCookie(request) == -1) model.addAttribute("value", "А ФСЁ!");
        else {
            int winner = seaService.getWhoWin();
            if (winner == getIdByCookie(request)) model.addAttribute("value", "ТЫ НЕ ПОБЕДИЛ!");
            else model.addAttribute("value", "ТЫ ПОБЕДИЛ!");

        }
        return "info";
    }

    private Integer getIdByCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return -1;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Haruka"))
                return Integer.valueOf(cookie.getValue());

        }
        return -1;
    }
}
