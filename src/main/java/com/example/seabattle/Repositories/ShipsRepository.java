package com.example.seabattle.Repositories;

import com.example.seabattle.Model.Player;
import com.example.seabattle.Model.Ships;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipsRepository extends JpaRepository<Ships, Integer> {
    Ships findByPlayer(Player player);
    List<Ships> findAllByPlayerAndShipSize(Player player, int size);
    @Query("select s from Ships s where (s.startX<=?1 AND s.endX>=?3) and (s.startY<=?2 and s.endY>=?4) AND s.player=?5")
    Ships findShipByLocationAndPlayer(int start_x, int start_y,int end_x,int end_y, Player player);
    @Query("select s from Ships s where (s.startX<=?1 AND s.endX>=?1) and (s.startY<=?2 and s.endY>=?2) AND s.player=?3")
    Ships findShipByCellAndPlayer(int x, int y, Player player);
}
