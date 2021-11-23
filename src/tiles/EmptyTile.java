package tiles;

import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;

import java.util.Optional;

/**
 * The EmptyTle represents the community and chance tiles from monopoly without any functionality
 * to diversify the board so its not just property tiles.
 */
public class EmptyTile implements GameTile {

    //Overrides function getName in GameTile interface and returns the name of the tile
    @Override
    public String getName() {
        return "Empty Tile";
    }

    /**
     * This method returns the set color the tile represents
     * @return the set color of the tile
     */
    @Override
    public PropertySet getPropertySet() {
        return PropertySet.White;
    }

    /**
     * This method returns nothing since this is an empty tile
     * @return null since tile is empty
     */
    @Override
    public PropertyTile getPropertyTile() {
        return null;
    }

    //Overrides function onLand in GameTile interface and does nothing since the tile does not trigger an event
    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players){
    }

    //Overrides function tileDescription in GameTile interface and returns a String of the Empty Tile's description
    @Override
    public String tileDescription() {
        return "Name: Empty Tile\nDescription: No event is linked with this tile.";
    }

    /**
     * This method returns an empty Optional since this is an empty tile
     * @return an empty Optional
     */
    @Override
    public Optional<BuyableTile> asBuyable() {
        return Optional.empty();
    }

    /**
     * This method returns an empty Optional since this is an empty tile
     * @return an empty Optional
     */
    @Override
    public Optional<HousingTile> asHousingTile() {
        return Optional.empty();
    }
}
