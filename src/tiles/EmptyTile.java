package tiles;

import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;

import java.util.Optional;

/**
 * The EmptyTle represents the community and chance tiles from monopoly without any functionality
 * to diversify the board so its not just property tiles.
 */
public class EmptyTile implements GameTileI {

    //Overrides function getName in GameTile interface and returns the name of the tile
    @Override
    public String getName() {
        return "Empty Tile";
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

    @Override
    public Optional<BuyableI> asBuyable() {
        return Optional.empty();
    }
}
