package tiles;

import gameLogic.GameBoard;
import gameLogic.Player;

public interface HousingTile extends BuyableTile {
    //adds a house to a property tile owned by the player
    void upgradeProperty(Player owner, GameBoard gameBoard);

    //returns the number of houses on a tile
    int numberOfHouses();

    boolean hasHotel();

    //gets the property set of the tile
    PropertySet getPropertySet();
}
