package tiles;

import gameLogic.GameBoard;
import gameLogic.Player;

public interface Buyable extends GameTile {
    void buy(Player player);
    void buyHouses(Player owner, GameBoard gameBoard);
    void transferOwnership(Player newOwner);
    void closeAuctionFor(int price, Player player);
    boolean isOwnedBy(Player player);
    boolean hasOwner();
    int getBuyCost();
}
