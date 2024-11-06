package com.example.seabattle.Model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name="ships")
@Data
public class Ships {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ship_id")
    private int shipId;
    @Column(name= "ship_size")
    private int shipSize;
    @Column(name="start_x")
    private int startX;
    @Column(name="start_y")
    private int startY;
    @Column(name="end_x")
    private int endX;
    @Column(name="end_y")
    private int endY;
    @Column(name="orientation")
    private int orientation;
    @Column(name="is_sunk")
    private boolean isSunk;
    @ManyToOne()
    @JoinColumn(name = "player_id")
    private Player player;

    public Ships(int shipSize, int startX, int startY, int endX, int endY, int orientation, boolean isSunk, Player player) {
        this.shipSize = shipSize;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.orientation = orientation;
        this.isSunk = isSunk;
        this.player = player;
    }

    public Ships() {

    }
}
