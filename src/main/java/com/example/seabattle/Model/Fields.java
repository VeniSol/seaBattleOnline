package com.example.seabattle.Model;

import lombok.Data;

@Data
public class Fields {
    int[][] fieldForFirstPlayer;
    int[][] fieldForSecondPlayer;

    public Fields(int[][] fieldForFirstPlayer, int[][] fieldForSecondPlayer) {
        this.fieldForFirstPlayer = fieldForFirstPlayer;
        this.fieldForSecondPlayer = fieldForSecondPlayer;
    }
}
