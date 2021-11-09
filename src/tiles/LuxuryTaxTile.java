package tiles;

import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;
import gameInterface.GameInterfaceI;

import java.util.Optional;

/**
 * The tiles.LuxuryTaxTile class represents the Luxury Tax Tile in the original game, where
 * $100 is deducted from the gameLogic.Player that lands on this tile. The amount deducted is added
 * to the total deposit in tiles.FreeParking.
 */
public class LuxuryTaxTile implements GameTileI {
    public static int baseCost = -100;

    private final FreeParking freeParking;
    private final GameInterfaceI gameInterface;

    /**This is the constructor for tiles.LuxuryTaxTile with parameters
     * @param gameInterface This provides text for each action the player takes
     * @param freeParking This is the Free Parking where the money gets added to
     */
    public LuxuryTaxTile(GameInterfaceI gameInterface, FreeParking freeParking) {
        this.freeParking = freeParking;
        this.gameInterface = gameInterface;
    }

    /**Overrides function onLand in tiles.GameTileI and deducts any gameLogic.Player's balance,
     * who lands on Luxury Tax Tile.
     * @param player This is the gameLogic.Player who lands on the Luxury Tax Tile
     * @param gameBoard This is the board in which the tile is situated
     * @param players These are the list of gameLogic.Players playing the game
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
        return "Name: Luxury Tax\nDescription: Pay $100";
    }

    /**Overrides function getName in tiles.GameTileI and returns the tile's
     * name
     * @return This returns the tile's name
     */
    @Override
    public String getName() {
        return "Luxury Tax";
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
