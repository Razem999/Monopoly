/**
 * The FreeParking class represents the Free Parking tile from the original
 * game, which accumulates taxes collected from players that land on Income
 * Tax Tile or Luxury Tax Tile, and deposits the total amount into a Player's
 * balance once they land on this tile.
 *
 * @author Razem Shahin
 * @version 1.0
 * @since 2021-10-25
 */
public class FreeParking implements GameTileI {
    public static int totalDeposited;

    private GameInterfaceI gameInterface;

    /**This is the constructor of FreeParking with a parameter
     * @param gameInterface This provides text for each action the player takes
     */
    FreeParking(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
    }

    /**This method adds the tax into the total tax accumulated
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
    public boolean tryBuy(Player player) {
        gameInterface.notifyCannotBuyTileKind(player, this);
        return false;
    }

    @Override
    public String getName() {
        return "Free Parking";
    }

    @Override
    public boolean isOwnedBy(Player player) {
        return false;
    }

    @Override
    public boolean tryTransferOwnership(Player newOwner) {
        return false;
    }

    @Override
    public boolean tryCloseAuctionFor(int price, Player player){
        return false;
    }

    @Override
    public boolean isAuctionable() {
        return false;
    }
}
