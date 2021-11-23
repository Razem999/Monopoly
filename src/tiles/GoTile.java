package tiles;

import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;
import gameInterface.GameInterface;

import java.util.Optional;

/**
 * The tiles.GoTile class represents the Go tile from the original game.
 * When a Player lands on/passes this tile, the Player receives the
 * Pass Go reward of $200.
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class GoTile implements GameTile {
    public static int passReward = 200;

    private final GameInterface gameInterface;

    /**This is the constructor of tiles.GoTile with a parameter
     * @param gameInterface This provides text for each action the player takes
     */
    public GoTile(GameInterface gameInterface) {
        this.gameInterface = gameInterface;
    }

    /**
     * This method is used to reward the Player with Go Money
     * @param player This is the player who gets rewarded
     * @param gameBoard This is the board where the Go Tile is situated
     * @param players This is the list of players playing the game
     */
    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        player.changeBalance(passReward);
        gameInterface.notifyPassGo(player);
    }

    /**
     * This method prints the tile's description
     * @return This returns the description as a string
     */
    @Override
    public String tileDescription() {
        return "Name: GO Tile\nDescription: Gain $200 when passing through.";
    }

    /**
     * This method is used to get the name of the tile
     * @return This returns the name as a string
     */
    @Override
    public String getName() {
        return "GO Tile";
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
