package com.example.seabattle.Repositories;

import com.example.seabattle.Model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player findByPlayerId(int id);
    Player findByPlayerName(String name);
    @Modifying
    @Transactional
    @Query("UPDATE Player p SET p.isActive = false ")
    void deactivateAllPlayers();
}
