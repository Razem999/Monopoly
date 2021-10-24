public interface GameInterfaceI {
    void startAuction(int tileNum, int startingBid);
    boolean processSale(String tileName, int amount, Player buyer);
    void notifyPlayerDeclinedPurchase(Player player, String tileName);
    void notifyPlayerPurchaseConfirm(Player player, String tileName, int amount);
    void notifyRentPayment(Player owner, Player payer, int amount);
    void notifyBankruptcy(Player player);
    void notifyRoll(Player player, int firstRoll, int secondRoll);
    void notifyPassGo(Player player);
    void notifyPlayerMovement(Player player, int tilesMoved, int newPosition, String destinationDescription);
    void notifyCannotRoll(Player player);
    void notifyCannotBuyTileKind(Player player, GameTileI tile);
    void notifyCannotBuyAlreadyOwned(Player player, Player owner, GameTileI tile);
    void notifyCannotBuyTileBalanceReasons(Player player, GameTileI tile);
    void notifyPlayerOwnsThis(Player owner);
    void notifyPlayerSentToJail(Player player);
    void notifyPlayerLeftJail(Player player);
    void notifyPlayerStayJail(Player player);
    void notifyFreeParkingDeposit(Player player, int amount);
}
