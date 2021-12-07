package tiles;

import gameLogic.Player;

public interface BuyableTile extends GameTile {
    void buy(Player player);
    void transferOwnership(Player newOwner);
    void closeAuctionFor(int price, Player player);
    boolean isOwnedBy(Player player);
    boolean hasOwner();
    int getBuyCost();
}
