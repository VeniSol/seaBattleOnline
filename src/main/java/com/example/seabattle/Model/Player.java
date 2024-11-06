package com.example.seabattle.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;


@Entity
@Table(name="players")
@Data
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private int playerId;
    @Column(name = "player_name")
    private String playerName;
    @Column(name = "is_turn")
    private Boolean isTurn;
    @Column(name = "is_active")
    private Boolean isActive;
}
