/**
 * The LuxuryTaxTile class represents the Luxury Tax Tile in the original game, where
 * $100 is deducted from the Player that lands on this tile. The amount deducted is added
 * to the total deposit in FreeParking.
 */
public class LuxuryTaxTile implements GameTileI{
    public static int baseCost = -100;

    private FreeParking freeParking;
    private GameInterfaceI gameInterface;

    /**This is the constructor for LuxuryTaxTile with parameters
     * @param gameInterface This provides text for each action the player takes
     * @param freeParking This is the Free Parking where the money gets added to
     */
    LuxuryTaxTile(GameInterfaceI gameInterface, FreeParking freeParking) {
        this.freeParking = freeParking;
        this.gameInterface = gameInterface;
    }

    /**Overrides function onLand in GameTileI and deducts any Player's balance,
     * who lands on Luxury Tax Tile.
     * @param player This is the Player who lands on the Luxury Tax Tile
     * @param gameBoard This is the board in which the tile is situated
     * @param players These are the list of Players playing the game
     */
    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        player.changeBalance(baseCost);
        freeParking.addTax(baseCost);
    }

    /**Overrides function tileDescription in GameTileI and prints the tile's
     * description
     * @return This returns the description of the tile
     */
    @Override
    public String tileDescription() {
        return "Name: Luxury Tax\nDescription: Pay $100";
    }

    /**Overrides function tryBuy in GameTileI and notifies Player that this cannot be
     * purchased
     * @param player This is the Player attempting to purchase the tile
     * @return This returns false
     */
    @Override
    public boolean tryBuy(Player player) {
        gameInterface.notifyCannotBuyTileKind(player, this);
        return false;
    }

    /**Overrides function getName in GameTileI and returns the tile's
     * name
     * @return This returns the tile's name
     */
    @Override
    public String getName() {
        return "Luxury Tax";
    }

    /**Overrides function isOwnedBy in GameTileI and returns false since this tile cannot be owned
     * @param player
     * @return This returns false
     */
    @Override
    public boolean isOwnedBy(Player player) {
        return false;
    }

    /**Overrides function tryTransferOwnership in GameTileI and returns false this tile cannot be
     * transferred
     * @param newOwner
     * @return This returns false
     */
    @Override
    public boolean tryTransferOwnership(Player newOwner) {
        return false;
    }

    /**Overrides function tryCloseAuctionFor in GameTileI and returns false this tile cannot be
     * auctioned for
     * @param price
     * @param player
     * @return This returns false
     */
    @Override
    public boolean tryCloseAuctionFor(int price, Player player) {
        return false;
    }

    /**Overrides function tryCloseAuctionFor in GameTileI and returns false this tile cannot be
     * auctioned for
     * @return This returns false
     */
    @Override
    public boolean isAuctionable() {
        return false;
    }
}
