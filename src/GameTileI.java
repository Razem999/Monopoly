public interface GameTileI {
    void onLand(Player player, GameBoard gameBoard, Players players);
    String tileDescription();
    boolean tryBuy(Player player);
    String getName();
    boolean isOwnedBy(Player player);
    boolean tryTransferOwnership(Player newOwner);
    boolean tryCloseAuctionFor(int price, Player player);
    boolean isAuctionable();
}
