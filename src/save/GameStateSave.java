package save;

import gameLogic.GameBoard;
import gameLogic.Players;

import java.io.Serializable;
import java.util.List;

public class GameStateSave implements Serializable {
    private final GameBoardSave gameBoardSave;
    private final PlayersSave playersSave;
    private final List<PropertyTileSave> propertyTileSavess;
    private final JailSave jailSave;
    private final List<RailroadSave> railroadSaves;
    private final int waterworksOwner;
    private final int electricCompanyOwner;
    private final int freeParkingDeposits;

    public GameStateSave(GameBoard gameBoard, Players players) {
        this.gameBoardSave = gameBoard.save();
        this.propertyTileSavess = gameBoard.savePropertyTiles();
        this.jailSave = gameBoard.saveJail();
        this.playersSave = players.save();
        this.railroadSaves = gameBoard.saveRailroads();
        this.waterworksOwner = gameBoard.saveWaterworks();
        this.electricCompanyOwner = gameBoard.saveElectricCompany();
        this.freeParkingDeposits = gameBoard.saveFreeParkingDeposits();
    }

    public GameBoardSave getGameBoardSave() {
        return gameBoardSave;
    }

    public PlayersSave getPlayersSave() {
        return playersSave;
    }

    public JailSave getJailSave() {
        return jailSave;
    }

    public List<PropertyTileSave> getPropertyTileSavess() {
        return propertyTileSavess;
    }

    public List<RailroadSave> getRailroadSaves() {
        return railroadSaves;
    }

    public int getElectricCompanyOwner() {
        return electricCompanyOwner;
    }

    public int getWaterworksOwner() {
        return waterworksOwner;
    }

    public int getFreeParkingDeposits() {
        return freeParkingDeposits;
    }
}
