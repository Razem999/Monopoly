package tiles;

import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;
import gameInterface.GameInterfaceI;

import java.util.Optional;

/**
 * The tiles.GoTile class represents the Go tile from the original game.
 * When a gameLogic.Player lands on/passes this tile, the gameLogic.Player receives the
 * Pass Go reward of $200.
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class GoTile implements GameTileI {
    public static int passReward = 200;

    private final GameInterfaceI gameInterface;

    /**This is the constructor of tiles.GoTile with a parameter
     * @param gameInterface This provides text for each action the player takes
     */
    public GoTile(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        player.changeBalance(passReward);
        gameInterface.notifyPassGo(player);
    }

    @Override
    public String tileDescription() {
        return "Name: GO Tile\nDescription: Gain $200 when passing through.";
    }

    @Override
    public String getName() {
        return "GO Tile";
    }

    @Override
    public PropertySet getPropertySet() {
        return PropertySet.White;
    }

    @Override
    public Optional<BuyableI> asBuyable() {
        return Optional.empty();
    }
}
