package save;

import gameLogic.Player;

import java.io.Serializable;

public class PlayerSave implements Serializable {
    private final int playerID;
    private final int balance;
    private final int tilePosition;

    public PlayerSave(int playerID, int balance, int tilePosition) {
        this.playerID = playerID;
        this.balance = balance;
        this.tilePosition = tilePosition;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getBalance() {
        return balance;
    }

    public int getTilePosition() {
        return tilePosition;
    }

    public Player getPlayer() {
        return new Player(playerID, balance, tilePosition);
    }
}
