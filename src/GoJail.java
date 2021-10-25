/**
 * The GoJail class represents the Go To Jail tile from the original
 * game. When a Player lands on this tile, the Player is sent directly
 * to jail and will not receive Pass Go reward.
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class GoJail implements GameTileI{

    private GameInterfaceI gameInterface;

    /**This is the constructor of GoJail with a parameter
     * @param gameInterface This provides text for each action the player takes
     */
    GoJail(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        gameBoard.jailPlayer(player);
    }

    @Override
    public String tileDescription() {
        return "Name: Go To Jail\nDescription: Go to Jail, Do not Pass GO.";
    }

    @Override
    public boolean tryBuy(Player player) {
        return false;
    }

    @Override
    public String getName() {
        return "Go To Jail";
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
