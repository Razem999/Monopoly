package tiles;

import gameLogic.GameBoard;
import gameLogic.Player;

public interface HousingTile extends BuyableTile {
    void upgradeProperty(Player owner, GameBoard gameBoard);
    int numberOfHouses();
    PropertySet getPropertySet();
}
