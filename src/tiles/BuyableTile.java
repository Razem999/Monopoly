package tiles;

import gameLogic.GameBoard;
import gameLogic.Player;

import java.util.Optional;

public interface BuyableTile extends GameTile {
    void buy(Player player);
    void transferOwnership(Player newOwner);
    void closeAuctionFor(int price, Player player);
    boolean isOwnedBy(Player player);
    boolean hasOwner();
    int getBuyCost();
}
