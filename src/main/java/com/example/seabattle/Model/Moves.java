package com.example.seabattle.Model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name="moves")
@Data
public class Moves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "move_id")
    private int moveId;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    @Column(name = "move_x")
    private int moveX;
    @Column(name = "move_y")
    private int moveY;

    public Moves(Player player, int moveX, int moveY) {
        this.player = player;
        this.moveX = moveX;
        this.moveY = moveY;
    }

    public Moves() {

    }
}
