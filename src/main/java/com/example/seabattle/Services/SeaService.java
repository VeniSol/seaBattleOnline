package com.example.seabattle.Services;

import com.example.seabattle.Model.Fields;
import com.example.seabattle.Model.Moves;
import com.example.seabattle.Model.Player;
import com.example.seabattle.Model.Ships;
import com.example.seabattle.Repositories.MovesRepository;
import com.example.seabattle.Repositories.PlayerRepository;
import com.example.seabattle.Repositories.ShipsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class SeaService {
    @Autowired
    ShipsRepository shipsRepository;
    @Autowired
    MovesRepository movesRepository;
    public int[][] fieldForFirstPlayer = new int[10][10];
    public int[][] fieldForSecondPlayer = new int[10][10];
    @Autowired
    private PlayerRepository playerRepository;
    int shipsOfTheFirstPlayer = 10;
    int shipsOfTheSecondPlayer = 10;
    @Getter
    private int whoWin = 0;
    private boolean isStartGame;


    public boolean savingShipLocation(int shipSize, int orientation, int move_x, int move_y, Player player) {
        if (isActiveShip(shipSize, player)) {
            shipSize--;
            int end_x = move_x, end_y = move_y;
            if (orientation == 1) {
                end_x = move_x + shipSize;
                if (move_x + shipSize >= 10)
                    return false;
            } else if (orientation == 2) {
                end_y = move_y + shipSize;
                if (move_y + shipSize >= 10)
                    return false;
            }
            if (isBusyShipLocation(move_x, move_y, end_x, end_y, player)) return false;
            shipsRepository.save(new Ships(shipSize + 1, move_x, move_y, end_x, end_y, orientation, false, player));
            if (orientation == 1) {
                for (int x = move_x; x <= end_x; x++) {
                    if (player.getPlayerId() == 1) fieldForFirstPlayer[x][move_y] = 1;
                    if (player.getPlayerId() == 2) fieldForSecondPlayer[x][move_y] = 1;
                }
            } else if (orientation == 2) {
                for (int y = move_y; y <= end_y; y++) {
                    if (player.getPlayerId() == 1) fieldForFirstPlayer[move_x][y] = 1;
                    if (player.getPlayerId() == 2) fieldForSecondPlayer[move_x][y] = 1;
                }
            }
            return true;
        }
        return false;
    }

    private boolean isActiveShip(int shipSize, Player player) {
        int[] ships = new int[]{4, 3, 2, 1};
        return shipsRepository.findAllByPlayerAndShipSize(player, shipSize).size() < ships[shipSize - 1];
    }

    private boolean isBusyShipLocation(int move_x, int move_y, int end_x, int end_y, Player player) {
        return shipsRepository.findShipByLocationAndPlayer(move_x, move_y, end_x, end_y, player) != null;
    }

    public boolean savingMovesLocation(int move_x, int move_y, Player player) {
        if (isBusyMoveLocation(move_x, move_y, player) || !player.getIsTurn()) return false;
        movesRepository.save(new Moves(player, move_x, move_y));
        if (player.getPlayerId() == 1)
            if (fieldForSecondPlayer[move_x][move_y] == 1) {
                fieldForSecondPlayer[move_x][move_y] = 4;
                Player player2 = playerRepository.findByPlayerId(2);
                if (isShipSunk(move_x, move_y, player2)) {
                    PaintSunkShip(move_x, move_y, player2);
                    shipsOfTheSecondPlayer--;
                    if (shipsOfTheSecondPlayer == 0) whoWin = player2.getPlayerId();
                }
            } else {
                fieldForSecondPlayer[move_x][move_y] = 3;
                changeMove();
            }
        if (player.getPlayerId() == 2)
            if (fieldForFirstPlayer[move_x][move_y] == 1) {
                fieldForFirstPlayer[move_x][move_y] = 4;
                Player player2 = playerRepository.findByPlayerId(1);
                if (isShipSunk(move_x, move_y, player2)) {
                    PaintSunkShip(move_x, move_y, player2);
                    shipsOfTheFirstPlayer--;
                    if (shipsOfTheFirstPlayer == 0) whoWin = player2.getPlayerId();
                }
            } else {
                fieldForFirstPlayer[move_x][move_y] = 3;
                changeMove();
            }

        return true;
    }

    private boolean isBusyMoveLocation(int move_x, int move_y, Player player) {
        int id = player.getPlayerId() == 1 ? 2 : 1;
        int statusCell = id == 1 ? fieldForFirstPlayer[move_x][move_y] : fieldForSecondPlayer[move_x][move_y];
        return statusCell != 0 && statusCell != 1;
    }

    private boolean isShipSunk(int x, int y, Player player) {
        Ships ship = shipsRepository.findShipByCellAndPlayer(x, y, player);
        if (ship.getOrientation() == 1) {
            for (int i = ship.getStartX(); i <= ship.getEndX(); i++) {
                if (player.getPlayerId() == 1) {
                    if (fieldForFirstPlayer[i][y] != 4) return false;
                } else if (player.getPlayerId() == 2) {
                    if (fieldForSecondPlayer[i][y] != 4) return false;
                }
            }
        }
        if (ship.getOrientation() == 2) {
            for (int j = ship.getStartY(); j <= ship.getEndY(); j++) {
                if (player.getPlayerId() == 1) {
                    if (fieldForFirstPlayer[x][j] != 4) return false;
                } else if (player.getPlayerId() == 2) {
                    if (fieldForSecondPlayer[x][j] != 4) return false;
                }
            }
        }
        return true;
    }

    private void PaintSunkShip(int x, int y, Player player) {
        Ships ship = shipsRepository.findShipByCellAndPlayer(x, y, player);
        if (ship.getOrientation() == 1) {
            for (int i = ship.getStartX() - 1; i <= ship.getEndX() + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    boolean checkX = i < 10 && i >= 0;
                    boolean checkY = j < 10 && j >= 0;
                    boolean checkShip = shipsRepository.findShipByCellAndPlayer(i, j, player) == null;
                    if (checkX && checkY && checkShip) {
                        if (player.getPlayerId() == 1) fieldForFirstPlayer[i][j] = 3;
                        else fieldForSecondPlayer[i][j] = 3;
                    }
                }
            }
        } else if (ship.getOrientation() == 2) {
            for (int i = ship.getStartY() - 1; i <= ship.getEndY() + 1; i++) {
                for (int j = x - 1; j <= x + 1; j++) {
                    boolean checkY = i < 10 && i >= 0;
                    boolean checkX = j < 10 && j >= 0;
                    boolean checkShip = shipsRepository.findShipByCellAndPlayer(j, i, player) == null;
                    if (checkX && checkY && checkShip) {
                        if (player.getPlayerId() == 1) fieldForFirstPlayer[j][i] = 3;
                        else fieldForSecondPlayer[j][i] = 3;
                    }
                }
            }
        }
    }

    public void randomShipGeneration() {
        for (int i = 1; i <= 2; i++) {
            Player player = playerRepository.findByPlayerId(i);
            int[] freeShips = new int[]{4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
            int shipSet = 0;
            int allShips = freeShips.length;
            while (shipSet < allShips) {
                Random rnd = new Random();
                int rndX = rnd.nextInt(10);
                int rndY = rnd.nextInt(10);
                int rndOrientation = rnd.nextInt(1, 3); // 1 - горизонтально, 2 - вертикально
                int size = freeShips[shipSet];
                boolean canPlaceShip = false;
                if (rndOrientation == 1 && rndX + size < 10) { // Горизонтально
                    canPlaceShip = presenceShip(rndX, rndY, size, 1, player);
                    if (canPlaceShip) {
                        savingShipLocation(size, 1, rndX, rndY, player);
                        shipSet++;
                    }
                } else if (rndOrientation == 2 && rndY + size < 10) { // Вертикально
                    canPlaceShip = presenceShip(rndX, rndY, size, 2, player);
                    if (canPlaceShip) {
                        savingShipLocation(size, 2, rndX, rndY, player);
                        shipSet++;
                    }
                }
            }
        }
    }

    private boolean presenceShip(int x, int y, int size, int orientation, Player player) {
        for (int i = 0; i < size; i++) {
            int checkX = (orientation == 1) ? x + i : x; // Горизонтально
            int checkY = (orientation == 1) ? y : y + i; // Вертикально
            if (!isInBounds(checkX, checkY) || !checkOneCell(checkX, checkY, player)) {
                return false;
            }
        }
        return true;
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    private boolean checkOneCell(int x, int y, Player player) {
        Ships ship = shipsRepository.findShipByCellAndPlayer(x, y, player);
        if (ship != null) return false;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (isInBounds(x + i, y + j)) {
                    if (shipsRepository.findShipByCellAndPlayer(x + i, y + j, player) != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public Fields getFields(int id) {
        if (!isStartGame) {
            startGame();
        }
        return id == 1 ? new Fields(fieldForFirstPlayer, fieldForSecondPlayer) : new Fields(fieldForSecondPlayer, fieldForFirstPlayer);

    }

    @Modifying
    public void startGame() {
        if (!isStartGame) {
            shipsRepository.deleteAll();
            movesRepository.deleteAll();
            playerRepository.deactivateAllPlayers();
            randomShipGeneration();
            isStartGame = true;
            if (!playerRepository.findByPlayerId(1).getIsTurn()) changeMove();
        }
    }

    @Modifying
    public void changeMove() {
        List<Player> players = playerRepository.findAll();
        for (Player player : players) {
            player.setIsTurn(!player.getIsTurn());
            playerRepository.save(player);
        }
    }

    public Player getCurrentPlayer(){
        List<Player> players = playerRepository.findAll();
        for (Player player:players) if(player.getIsTurn()) return player;
        return null;
    }
}



