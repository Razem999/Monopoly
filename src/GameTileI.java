public interface GameTileI {
    void onLand(Player player, GameBoard gameBoard, Players players);
    String tileDescription();
    boolean tryBuy(Player player);
    String getName();
    boolean isOwnedBy(Player player);
}
