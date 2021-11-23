package tiles;

import java.util.Optional;

import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;

/**
 * The tiles.GoJail class represents the Go To Jail tile from the original
 * game. When a Player lands on this tile, the Player is sent directly
 * to jail and will not receive Pass Go reward.
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class GoJail implements GameTile {

    /**
     * This method is used to send the player that lands on this tile to Jail
     * @param player This is the player that is sent to Jail
     * @param gameBoard This is the board where the GoJail tile is situated
     * @param players This is the list of players playing the game
     */
    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        gameBoard.jailPlayer(player);
    }

    /**
     * This method prints the tile's description
     * @return This returns the description as a string
     */
    @Override
    public String tileDescription() {
        return "Name: Go To Jail\nDescription: Go to Jail, Do not Pass GO.";
    }

    /**
     * This method is used to get the name of the tile
     * @return This returns the name as a string
     */
    @Override
    public String getName() {
        return "Go To Jail";
    }

    /**
     * This method is used to get the set color this property represents
     * @return This returns the PropertySet
     */
    @Override
    public PropertySet getPropertySet() {
        return PropertySet.White;
    }

    /**
     * This method is used to get the property in this tile
     * @return This returns null since this is not buyable
     */
    @Override
    public PropertyTile getPropertyTile() {
        return null;
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
