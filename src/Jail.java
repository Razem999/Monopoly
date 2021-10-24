import java.util.Optional;

public class Jail implements GameTileI{

    public static int jailFine = 50;
    private boolean inJail;

    private GameInterfaceI gameInterfaceI;
    private Optional<Player> players;

    public Jail(GameInterfaceI gameInterfaceI) {
        this.inJail = false;
        this.gameInterfaceI = gameInterfaceI;
    }

    public void setPlayerToJail(Player player) {

    }

    private void tryBail() {

    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        //do nothing
    }

    @Override
    public String tileDescription() {
        return "Name: Jail\n" +
                "Description: Just Visiting Jail.";
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
}
