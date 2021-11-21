package gameInterface;

import tiles.Buyable;
import tiles.GameTile;
import gameLogic.GameBoard;
import gameLogic.Players;
import gameLogic.Player;

public interface GameInterface extends AuctionBidExecutor {
    void startAuction(int startingBid, Buyable tile, Players players, int tilePosition);
    boolean processSale(String tileName, int amount, Player buyer);
    void displayPlayerProperties(Player player, GameBoard gameBoard);
    void notifyPlayerDeclinedPurchase(Player player, String tileName);
    void notifyPlayerPurchaseConfirm(Player player, String tileName, int amount);
    void notifyRentPayment(Player owner, Player payer, int amount);
    void notifyBankruptcy(Player player);
    void notifyRoll(Player player, int firstRoll, int secondRoll);
    void notifyPassGo(Player player);
    void notifyPlayerMovement(Player player, int tilesMoved, int newPosition, String destinationDescription);
    void notifyCannotRoll(Player player);
    void notifyCannotBuyTileKind(Player player, GameTile tile);
    void notifyCannotBuyAlreadyOwned(Player player, Player owner, GameTile tile);
    void notifyCannotBuyTileBalanceReasons(Player player, GameTile tile);
    void notifyPlayerOwnsThis(Player owner);
    void notifyPlayerSentToJail(Player player);
    void notifyPlayerLeftJail(Player player);
    void notifyPlayerStayJail(Player player);
    void notifyFreeParkingDeposit(Player player, int amount);
    void notifyAuctionCannotStart(GameTile tile);
    void notifyPlayerTaxPayment(Player player, int amount);
    void notifyPlayerEndedTurn(Player player);
    void notifyPlayerTurn(Player player);
    void notifyPlayerMustRoll(Player player);
    PlayerSelection askHowManyPlayers();
    void notifyBetError(String msg);
}
