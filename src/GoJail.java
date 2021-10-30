import java.util.Optional;

/**
 * The GoJail class represents the Go To Jail tile from the original
 * game. When a Player lands on this tile, the Player is sent directly
 * to jail and will not receive Pass Go reward.
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class GoJail implements GameTileI {

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        gameBoard.jailPlayer(player);
    }

    @Override
    public String tileDescription() {
        return "Name: Go To Jail\nDescription: Go to Jail, Do not Pass GO.";
    }

    @Override
    public String getName() {
        return "Go To Jail";
    }

    @Override
    public Optional<BuyableI> asBuyable() {
        return Optional.empty();
    }
}
