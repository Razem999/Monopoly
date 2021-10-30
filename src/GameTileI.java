import java.util.Optional;

public interface GameTileI {
    void onLand(Player player, GameBoard gameBoard, Players players);
    String tileDescription();
    String getName();
    Optional<BuyableI> asBuyable();
}
