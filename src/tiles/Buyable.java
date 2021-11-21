package tiles;

import gameLogic.Player;

public interface Buyable extends GameTile {
    void buy(Player player);
    void transferOwnership(Player newOwner);
    void closeAuctionFor(int price, Player player);
    boolean isOwnedBy(Player player);
    boolean hasOwner();
    int getBuyCost();
}
