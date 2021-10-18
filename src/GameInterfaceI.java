public interface GameInterfaceI {
    void startAuction(int tileNum, int startingBid);
    boolean processSale(String tileName, int amount, Player buyer);
    void notifyRentPayment(Player owner, Player payer, int amount);
    void notifyBankruptcy(Player player);
    void notifyRoll(Player player, int firstRoll, int secondRoll);
    void notifyPassGo(Player player);
    void notifyPlayerMovement(Player player, int tilesMoved, int newPosition, String destinationDescription);
}
