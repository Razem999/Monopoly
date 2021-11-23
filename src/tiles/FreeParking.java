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

    /**
     * This method is used to deposit the total tax accumulated to the player that lands on this tile
     * @param player This is the Player who lands on the tile
     * @param gameBoard This is the board where the tile is situated
     * @param players This is the list players playing the game
     */
    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        player.changeBalance(totalDeposited);
        gameInterface.notifyFreeParkingDeposit(player, totalDeposited);
        totalDeposited = 0;
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
        return "Free Parking";
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
