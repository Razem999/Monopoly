package tiles;

import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;

import java.util.Optional;

public interface GameTileI {
    void onLand(Player player, GameBoard gameBoard, Players players);
    String tileDescription();
    String getName();
    Optional<BuyableI> asBuyable();
}
