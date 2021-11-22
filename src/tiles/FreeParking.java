package tiles;

import gameInterface.GameInterface;
import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;

import java.util.Optional;

/**
 * The tiles.FreeParking class represents the Free Parking tile from the original
 * game, which accumulates taxes collected from players that land on Income
 * Tax Tile or Luxury Tax Tile, and deposits the total amount into a Player's
 * balance once they land on this tile.
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class FreeParking implements GameTile {
    public static int totalDeposited;

    private final GameInterface gameInterface;

    /**This is the constructor of tiles.FreeParking with a parameter
     * @param gameInterface This provides text for each action the player takes
     */
    public FreeParking(GameInterface gameInterface) {
        this.gameInterface = gameInterface;
    }

    /**This method adds the tax into the total tax accumulated.
     * @param amount This is the amount taxed from a Player
     */
    public void addTax(int amount) {
        totalDeposited += amount;
    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        player.changeBalance(totalDeposited);
        gameInterface.notifyFreeParkingDeposit(player, totalDeposited);
        totalDeposited = 0;
    }

    @Override
    public String tileDescription() {
        return "Name: GO Tile\nDescription: Gain $200 when passing through.";
    }

    @Override
    public String getName() {
        return "Free Parking";
    }

    @Override
    public PropertySet getPropertySet() {
        return PropertySet.White;
    }

    @Override
    public PropertyTile getPropertyTile() {
        return null;
    }

    @Override
    public Optional<Buyable> asBuyable() {
        return Optional.empty();
    }
}
