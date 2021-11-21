package gameInterface;

import gameLogic.GameBoard;
import gameLogic.Player;
import gameLogic.Players;
import tiles.BuyableI;
import tiles.GameTileI;

import java.util.ArrayList;
import java.util.List;

/**
 * Any output only operations are forwarded to all backing interfaces while only one interface will try to get input.
 */
public class CompoundGameInterface implements GameInterfaceI {

    private final List<GameInterfaceI> backingInterfaces;

    public CompoundGameInterface() {
        this.backingInterfaces = new ArrayList<>();
    }

    public void connectGameInterface(GameInterfaceI gameInterface) {
        this.backingInterfaces.add(gameInterface);
    }

    @Override
    public void startAuction(int startingBid, BuyableI tile, Players players) {
        this.backingInterfaces.forEach(i -> i.startAuction(startingBid, tile, players));
    }

    @Override
    public boolean processSale(String tileName, int amount, Player buyer) {
        if (this.backingInterfaces.size() > 0) {
            return this.backingInterfaces.get(0).processSale(tileName, amount, buyer);
        }

        return false;
    }

    @Override
    public void displayPlayerProperties(Player player, GameBoard gameBoard) {
        this.backingInterfaces.forEach(i -> i.displayPlayerProperties(player, gameBoard));
    }

    @Override
    public void notifyPlayerDeclinedPurchase(Player player, String tileName) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerDeclinedPurchase(player, tileName));
    }

    @Override
    public void notifyPlayerPurchaseConfirm(Player player, String tileName, int amount) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerPurchaseConfirm(player, tileName, amount));
    }

    @Override
    public void notifyRentPayment(Player owner, Player payer, int amount) {
        this.backingInterfaces.forEach(i -> i.notifyRentPayment(owner, payer, amount));
    }

    @Override
    public void notifyBankruptcy(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyBankruptcy(player));
    }

    @Override
    public void notifyRoll(Player player, int firstRoll, int secondRoll) {
        this.backingInterfaces.forEach(i -> i.notifyRoll(player, firstRoll, secondRoll));
    }

    @Override
    public void notifyPassGo(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPassGo(player));
    }

    @Override
    public void notifyPlayerMovement(Player player, int tilesMoved, int newPosition, String destinationDescription) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerMovement(player, tilesMoved, newPosition, destinationDescription));
    }

    @Override
    public void notifyCannotRoll(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyCannotRoll(player));
    }

    @Override
    public void notifyCannotBuyTileKind(Player player, GameTileI tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyTileKind(player, tile));
    }

    @Override
    public void notifyCannotBuyAlreadyOwned(Player player, Player owner, GameTileI tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyAlreadyOwned(player, owner, tile));
    }

    @Override
    public void notifyCannotBuyTileBalanceReasons(Player player, GameTileI tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyTileBalanceReasons(player, tile));
    }

    @Override
    public void notifyPlayerOwnsThis(Player owner) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerOwnsThis(owner));
    }

    @Override
    public void notifyPlayerSentToJail(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerSentToJail(player));
    }

    @Override
    public void notifyPlayerLeftJail(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerLeftJail(player));
    }

    @Override
    public void notifyPlayerStayJail(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerStayJail(player));
    }

    @Override
    public void notifyFreeParkingDeposit(Player player, int amount) {
        this.backingInterfaces.forEach(i -> i.notifyFreeParkingDeposit(player, amount));
    }

    @Override
    public void notifyAuctionCannotStart(GameTileI tile) {
        this.backingInterfaces.forEach(i -> i.notifyAuctionCannotStart(tile));
    }

    @Override
    public void notifyAuctionBetLow(Player player, int amount) {
        this.backingInterfaces.forEach(i -> i.notifyAuctionBetLow(player, amount));
    }

    @Override
    public void notifyPlayerTaxPayment(Player player, int amount) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerTaxPayment(player, amount));
    }

    @Override
    public void notifyPlayerEndedTurn(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerEndedTurn(player));
    }

    @Override
    public void notifyPlayerTurn(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerTurn(player));
    }

    @Override
    public void notifyPlayerMustRoll(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerMustRoll(player));
    }

    @Override
    public PlayerSelection askHowManyPlayers() {
        if (this.backingInterfaces.size() > 0) {
            return this.backingInterfaces.get(0).askHowManyPlayers();
        }

        return new PlayerSelection(4, 0);
    }
}
