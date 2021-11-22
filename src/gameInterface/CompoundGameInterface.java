package gameInterface;

import gameLogic.Auction;
import gameLogic.GameBoard;
import gameLogic.Player;
import gameLogic.Players;
import tiles.BuyableTile;
import tiles.GameTile;
import tiles.PropertyTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Any output only operations are forwarded to all backing interfaces while only one interface will try to get input.
 */
public class CompoundGameInterface implements GameInterface {

    private final List<GameInterface> backingInterfaces;

    public CompoundGameInterface() {
        this.backingInterfaces = new ArrayList<>();
    }

    public void connectGameInterface(GameInterface gameInterface) {
        this.backingInterfaces.add(gameInterface);
    }

    @Override
    public void startAuction(int startingBid, BuyableTile tile, Players players, int tilePosition) {
        this.backingInterfaces.forEach(i -> i.startAuction(startingBid, tile, players, tilePosition));
    }

    @Override
    public boolean processSale(String tileName, int amount, Player player) {
        if (player.getAIStrategy().isPresent()) {
            return true;
        }
        if (this.backingInterfaces.size() > 0) {
            return this.backingInterfaces.get(0).processSale(tileName, amount, player);
        }

        return false;
    }

    @Override
    public Optional<Integer> processHouseSale(List<GameBoard.TileAndIndex> tiles, Player player, GameBoard gameBoard) {
        if (this.backingInterfaces.size() > 0) {
            return this.backingInterfaces.get(0).processHouseSale(tiles, player, gameBoard);
        }

        return Optional.empty();
    }

    @Override
    public boolean processHotelSale(String tileName, int amount, int currentNumHouses, int currentNumHotels, Player player) {
        if (player.getAIStrategy().isPresent()) {
            return true;
        }
        if (this.backingInterfaces.size() > 0) {
            return this.backingInterfaces.get(0).processHotelSale(tileName, amount, currentNumHouses, currentNumHotels, player);
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
    public void notifyCannotBuyTileKind(Player player, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyTileKind(player, tile));
    }

    @Override
    public void notifyCannotBuyAlreadyOwned(Player player, Player owner, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyAlreadyOwned(player, owner, tile));
    }

    @Override
    public void notifyCannotBuyTileBalanceReasons(Player player, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyTileBalanceReasons(player, tile));
    }

    @Override
    public void notifyCannotBuyHouseBalanceReasons(Player player, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyHouseBalanceReasons(player, tile));
    }

    @Override
    public void notifyCannotBuyHouseOwnershipReasons(Player player, Optional<Player> owner, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyHouseOwnershipReasons(player, owner, tile));
    }

    @Override
    public void notifyCannotBuyHouseTileKind(Player player, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyHouseTileKind(player, tile));
    }

    @Override
    public void notifyCannotBuyHouseSetReasons(Player player, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyHouseSetReasons(player, tile));
    }

    @Override
    public void notifyHousesUnavailable(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyHousesUnavailable(player));
    }

    @Override
    public void notifyHotelsUnavailable(Player player) {
        this.backingInterfaces.forEach(i -> notifyHotelsUnavailable(player));
    }

    @Override
    public void notifyPlayerPurchasedHouse(Player player, String tileName, int amount) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerPurchasedHouse(player, tileName, amount));
    }

    @Override
    public void notifyPlayerDeclinedHouse(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerDeclinedHouse(player));
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
    public void notifyAuctionCannotStart(GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyAuctionCannotStart(tile));
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

    @Override
    public void notifyBetError(String msg) {
        this.backingInterfaces.forEach(i -> i.notifyBetError(msg));
    }

    @Override
    public void notifyTileCannotUpgradeFurther(Player player, PropertyTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyTileCannotUpgradeFurther(player, tile));
    }

    @Override
    public Auction.BidAdvanceToken doPlayerBid(Auction auction, Players players, int tilePosition) {
        if (this.backingInterfaces.size() > 0) {
            return this.backingInterfaces.get(0).doPlayerBid(auction, players, tilePosition);
        } else {
            throw new IllegalStateException("UNINITIALIZED COMPOUND INTERFACE, AUCTION REQUIRES INITIALIZED INTERFACE");
        }
    }
}
