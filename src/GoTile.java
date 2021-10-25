/**
 * The GoTile class represents the Go tile from the original game.
 * When a Player lands on/passes this tile, the Player receives the
 * Pass Go reward of $200.
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class GoTile implements GameTileI {
    public static int passReward = 200;

    private GameInterfaceI gameInterface;

    /**This is the constructor of GoTile with a parameter
     * @param gameInterface This provides text for each action the player takes
     */
    GoTile(GameInterfaceI gameInterface) {
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
    public boolean tryBuy(Player player) {
        gameInterface.notifyCannotBuyTileKind(player, this);
        return false;
    }

    @Override
    public String getName() {
        return "GO Tile";
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
