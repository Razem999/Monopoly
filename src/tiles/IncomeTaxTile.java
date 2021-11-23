package tiles;

import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;
import gameInterface.GameInterface;

import java.util.Optional;

/**
 * The tiles.IncomeTaxTile class represents the Income Tax Tile in the original game, where
 * $200 is deducted from the Player that lands on this tile. The amount deducted is added
 * to the total deposit in tiles.FreeParking.
 */
public class IncomeTaxTile implements GameTile {
    public static int baseCost = -200;

    private final FreeParking freeParking;
    private final GameInterface gameInterface;

    /**This is the constructor for tiles.IncomeTaxTile with parameters
     * @param gameInterface This provides text for each action the player takes
     * @param freeParking This is the Free Parking where the money gets added to
     */
    public IncomeTaxTile(GameInterface gameInterface, FreeParking freeParking) {
        this.freeParking = freeParking;
        this.gameInterface = gameInterface;
    }

    /**Overrides function onLand in tiles.GameTileI and deducts any Player's balance,
     * who lands on Income Tax Tile.
     * @param player This is the Player who lands on the Income Tax Tile
     * @param gameBoard This is the board in which the tile is situated
     * @param players These are the list of Players playing the game
     */
    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        player.changeBalance(baseCost);
        freeParking.addTax(baseCost);
        this.gameInterface.notifyPlayerTaxPayment(player, baseCost);
    }

    /**Overrides function tileDescription in tiles.GameTileI and prints the tile's
     * description
     * @return This returns the description of the tile
     */
    @Override
    public String tileDescription() {
        return "Name: Income Tax\nDescription: Pay $200";
    }

    /**Overrides function getName in tiles.GameTileI and returns the tile's
     * name
     * @return This returns the tile's name
     */
    @Override
    public String getName() {
        return "Income Tax";
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
