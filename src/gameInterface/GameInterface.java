package gameInterface;

import tiles.BuyableTile;
import tiles.GameTile;
import gameLogic.GameBoard;
import gameLogic.Players;
import gameLogic.Player;
import tiles.PropertyTile;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface GameInterface extends AuctionBidExecutor {
    void startAuction(int startingBid, BuyableTile tile, Players players, int tilePosition);
    boolean processSale(String tileName, int amount, Player player);
    void getTileSelection(List<GameBoard.TileAndIndex> tiles, GameBoard gameBoard, Consumer<Optional<Integer>> onSelection);
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
    void notifyCannotBuyHouseBalanceReasons(Player player, GameTile tile);
    void notifyCannotBuyHouseOwnershipReasons(Player player, Optional<Player> owner, GameTile tile);
    void notifyCannotBuyHouseTileKind(Player player, GameTile tile);
    void notifyCannotBuyHouseSetReasons(Player player, GameTile tile);
    void notifyHousesUnavailable(Player player);
    void notifyHotelsUnavailable(Player player);
    void notifyPlayerPurchasedHouse(Player player, String tileName, int amount);
    void notifyPlayerDeclinedHouse(Player player);
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
    void notifyTileCannotUpgradeFurther(Player player, PropertyTile tile);
    void notifyNoTilesApplicable();
}
