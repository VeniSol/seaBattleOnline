package com.example.seabattle.RequestBody;

import com.example.seabattle.Model.Moves;
import com.example.seabattle.Model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoveBody {
    private int x;
    private int y;
}
