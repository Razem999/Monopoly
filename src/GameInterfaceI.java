public interface GameInterfaceI {
    void startAuction(int tileNum, int startingBid);
    void processSale(int tileNum, int amount, Player buyer);
}
