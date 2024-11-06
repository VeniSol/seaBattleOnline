package com.example.seabattle.Repositories;

import com.example.seabattle.Model.Moves;
import com.example.seabattle.Model.Player;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovesRepository  extends JpaRepository<Moves, Integer> {
    List<Moves> findByPlayer(Player player);
    Moves findByPlayerAndMoveXAndMoveY(Player player, int moveX, int moveY);
}
