/**
 * The Jail class represents the Jail/Just Visiting tile from the original
 * game. When a Player lands on Go To Jail tile, or rolls doubles three times,
 * Then the Player will move their position to this tile and movements will be
 * restricted once they are inside jail (not Just Visiting).
 *
 * @version 1.0
 * @since 2021-10-25
 */
import java.util.Optional;

public class Jail implements GameTileI{

    public static int jailFine = 50;
    private boolean inJail;

    private GameInterfaceI gameInterfaceI;
    private Optional<Player> players;

    /**This is the constructor of Jail with a parameter
     * @param gameInterfaceI This provides text for each action the player takes
     */
    public Jail(GameInterfaceI gameInterfaceI) {
        this.inJail = false;
        this.gameInterfaceI = gameInterfaceI;
        this.players = Optional.empty();
    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        //do nothing
    }

    @Override
    public String tileDescription() {
        if (this.players.get().isInJail()) {
            return "Name: Jail\n" +
                    "Description: In Jail";
        } else {
            return "Name: Jail\n" +
                    "Description: Just Visiting Jail.";
        }
    }

    @Override
    public boolean tryBuy(Player player) {
        return false;
    }

    @Override
    public String getName() {
        return "Just Visiting Jail/Jail";
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
