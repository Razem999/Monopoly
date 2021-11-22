package tiles;

import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;

import java.util.Optional;

public interface GameTile {
    void onLand(Player player, GameBoard gameBoard, Players players);
    String tileDescription();
    String getName();
    PropertySet getPropertySet();
    PropertyTile getPropertyTile();
    Optional<BuyableTile> asBuyable();
    Optional<HousingTile> asHousingTile();
}
