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
import java.util.function.Consumer;

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

    /**This method is used to print the information about the auction when it begins
     * @param startingBid the starting bid of the auction
     * @param tile The tile being sold at auction
     * @param players The player making the purchase
     * @param tilePosition the position of the tile being sold on the board
     */
    @Override
    public void startAuction(int startingBid, BuyableTile tile, Players players, int tilePosition) {
        this.backingInterfaces.forEach(i -> i.startAuction(startingBid, tile, players, tilePosition));
    }

    /**This method is used to print the information about property transactions
     * @param tileName the name of the tile the transaction is occuring at
     * @param amount The amount the transaction costs
     * @param player The player making the purchase
     */
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

    /**This method is used to pop up a list of tiles
     * @param tiles the tiles present for selection
     * @param gameBoard Gives information about tiles on the board
     * @param onSelection A callback for someone who requests a selection menu to be made to do things with the results.
     */
    @Override
    public void getTileSelection(List<GameBoard.TileAndIndex> tiles, GameBoard gameBoard, Consumer<Optional<Integer>> onSelection) {
        if (this.backingInterfaces.size() > 0) {
            this.backingInterfaces.get(0).getTileSelection(tiles, gameBoard, onSelection);
        }
    }

    /**This method is used to print the properties that a player owns
     * @param player the player whose information will be displayed
     * @param gameBoard Gives information about tiles on the board
     */
    @Override
    public void displayPlayerProperties(Player player, GameBoard gameBoard) {
        this.backingInterfaces.forEach(i -> i.displayPlayerProperties(player, gameBoard));
    }

    /**This method is used to print that the user has declined to purchase a tile
     * @param player the player whose information will be displayed
     * @param tileName the name of the tile the player has refused to buy
     */
    @Override
    public void notifyPlayerDeclinedPurchase(Player player, String tileName) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerDeclinedPurchase(player, tileName));
    }

    /**This method is used to print that the purchase was successful
     * @param player the player that made the purchase
     * @param tileName the tile that was purchased
     * @param amount the cost of the transaction
     */
    @Override
    public void notifyPlayerPurchaseConfirm(Player player, String tileName, int amount) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerPurchaseConfirm(player, tileName, amount));
    }

    /**This method is used that the player has paid rent
     * @param player the player that made the purchase
     * @param owner the owner of the tile who has received the rent payment
     * @param amount the cost of the transaction
     */
    @Override
    public void notifyRentPayment(Player owner, Player player, int amount) {
        this.backingInterfaces.forEach(i -> i.notifyRentPayment(owner, player, amount));
    }

    /**This method is used to print that the player has gone bankrupt
     * @param player the player that has gone bankrupt
     */
    @Override
    public void notifyBankruptcy(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyBankruptcy(player));
    }

    /**This method is used to print the results of the player's roll
     * @param player the player that rolled
     * @param firstRoll the value rolled by the first dice
     * @param secondRoll the value rolled by the second dice
     */
    @Override
    public void notifyRoll(Player player, int firstRoll, int secondRoll) {
        this.backingInterfaces.forEach(i -> i.notifyRoll(player, firstRoll, secondRoll));
    }

    /**This method is used to print that the player has passed go
     * @param player the player that has passed go
     */
    @Override
    public void notifyPassGo(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPassGo(player));
    }

    /**This method is used to print new position that the player has landed on
     * @param player the player that has moved
     * @param tilesMoved the number of tiles the player has moved
     * @param newPosition the new position on the board
     * @param destinationDescription the description of the tile the player has moved too
     */
    @Override
    public void notifyPlayerMovement(Player player, int tilesMoved, int newPosition, String destinationDescription) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerMovement(player, tilesMoved, newPosition, destinationDescription));
    }

    /**This method is used to print that the player cannot roll the dice
     * @param player the player that requested to roll the dice
     */
    @Override
    public void notifyCannotRoll(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyCannotRoll(player));
    }

    /**This method is used to print that a tile cannot be bought
     * @param player the player that has attempted to buy the property
     * @param tile the tile that cannot be bought
     */
    @Override
    public void notifyCannotBuyTileKind(Player player, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyTileKind(player, tile));
    }

    /**This method is used to print that a tile cannot be bought for the reason that it is already owned
     * @param player the player that has attempted to buy the property
     * @param owner the owner of the tile
     * @param tile the tile that cannot be bought
     */
    @Override
    public void notifyCannotBuyAlreadyOwned(Player player, Player owner, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyAlreadyOwned(player, owner, tile));
    }

    /**This method is used to print that a tile cannot be bought for the reason that the player is short on funds
     * @param player the player that has attempted to buy the property
     * @param tile the tile that cannot be bought
     */
    @Override
    public void notifyCannotBuyTileBalanceReasons(Player player, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyTileBalanceReasons(player, tile));
    }

    /**This method is used to print that a house cannot be bought for the reason that the player is short on funds
     * @param player the player that has attempted to buy the house
     * @param tile the tile where the house would be placed
     */
    @Override
    public void notifyCannotBuyHouseBalanceReasons(Player player, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyHouseBalanceReasons(player, tile));
    }

    /**This method is used to print that a house cannot be bought for the reason that the player does not own the property
     * @param player the player that has attempted to buy the house
     * @param owner the owner of the property if there is one
     * @param tile the tile where the house would be placed
     */
    @Override
    public void notifyCannotBuyHouseOwnershipReasons(Player player, Optional<Player> owner, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyHouseOwnershipReasons(player, owner, tile));
    }

    /**This method is used to print that a house cannot be bought for the reason that the tile cannot have houses
     * @param player the player that has attempted to buy the house
     * @param tile the tile that cannot have houses
     */
    @Override
    public void notifyCannotBuyHouseTileKind(Player player, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyHouseTileKind(player, tile));
    }

    /**This method is used to print that a house cannot be bought for the reason that the player does not own the property set
     * @param player the player that has attempted to buy the house
     * @param tile the tile where the house would be placed
     */
    @Override
    public void notifyCannotBuyHouseSetReasons(Player player, GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyCannotBuyHouseSetReasons(player, tile));
    }

    /**This method is used to print that a house cannot be bought for the reason that there are no more houses to be placed on the board
     * @param player the player that has attempted to buy the house
     */
    @Override
    public void notifyHousesUnavailable(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyHousesUnavailable(player));
    }

    /**This method is used to print that a hotel cannot be purchased
     * @param player the player that has attempted to buy the hotel
     */
    @Override
    public void notifyHotelsUnavailable(Player player) {
        this.backingInterfaces.forEach(i -> notifyHotelsUnavailable(player));
    }

    /**This method is used to print that a house has been bought
     * @param player the player that has attempted to buy the house
     * @param tileName the name of the tile where the house was placed
     * @param amount the amount the house costs
     */
    @Override
    public void notifyPlayerPurchasedHouse(Player player, String tileName, int amount) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerPurchasedHouse(player, tileName, amount));
    }

    /**This method is used to print that the player has declined the purchase of a house
     * @param player the player that has declined to buy a house
     */
    @Override
    public void notifyPlayerDeclinedHouse(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerDeclinedHouse(player));
    }

    /**This method is used to print that a player owns this tile
     * @param owner the owner of the property
     */
    @Override
    public void notifyPlayerOwnsThis(Player owner) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerOwnsThis(owner));
    }

    /**This method is used to print that a player went to jail
     * @param player the player that went to jail
     */
    @Override
    public void notifyPlayerSentToJail(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerSentToJail(player));
    }

    /**This method is used to print that a player left jail
     * @param player the player that left jail
     */
    @Override
    public void notifyPlayerLeftJail(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerLeftJail(player));
    }

    /**This method is used to print that a player stayed jail
     * @param player the player that stayed jail
     */
    @Override
    public void notifyPlayerStayJail(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerStayJail(player));
    }

    /**This method is used to print that a player deposited money into freeparking
     * @param player the player that deposited money
     * @param amount the amount deposited into freeparking
     */
    @Override
    public void notifyFreeParkingDeposit(Player player, int amount) {
        this.backingInterfaces.forEach(i -> i.notifyFreeParkingDeposit(player, amount));
    }

    /**This method is used to print that an auction cannot be started
     * @param tile the tile that cannot be auctioned
     */
    @Override
    public void notifyAuctionCannotStart(GameTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyAuctionCannotStart(tile));
    }

    /**This method is used to print that a player has paid taxes
     * @param player the player that paid taxes
     * @param amount the amount the player paid
     */
    @Override
    public void notifyPlayerTaxPayment(Player player, int amount) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerTaxPayment(player, amount));
    }

    /**This method is used to print that a player ended their turn
     * @param player the player that ended their turn
     */
    @Override
    public void notifyPlayerEndedTurn(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerEndedTurn(player));
    }

    /**This method is used to print that a player has their turn now
     * @param player the player whose turn it is
     */
    @Override
    public void notifyPlayerTurn(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerTurn(player));
    }

    /**This method is used to print that a player still must roll the dice
     * @param player the player that need to roll
     */
    @Override
    public void notifyPlayerMustRoll(Player player) {
        this.backingInterfaces.forEach(i -> i.notifyPlayerMustRoll(player));
    }

    /**This method is used to print a request to ask how many players want to play the game
     */
    @Override
    public PlayerSelection askHowManyPlayers() {
        if (this.backingInterfaces.size() > 0) {
            return this.backingInterfaces.get(0).askHowManyPlayers();
        }

        return new PlayerSelection(4, 0);
    }

    /**This method is used to print that there was an error when bidding
     * @param msg the error message
     */
    @Override
    public void notifyBetError(String msg) {
        this.backingInterfaces.forEach(i -> i.notifyBetError(msg));
    }

    /**This method is used to print that a tile has the maximum building on it
     * @param player the player that attempted to buy a house
     * @param tile that is maxed out on houses
     */
    @Override
    public void notifyTileCannotUpgradeFurther(Player player, PropertyTile tile) {
        this.backingInterfaces.forEach(i -> i.notifyTileCannotUpgradeFurther(player, tile));
    }

    /**This method is used to print that a player does not own a tile to build a house on
     */
    @Override
    public void notifyNoTilesApplicable() {
        this.backingInterfaces.forEach(GameInterface::notifyNoTilesApplicable);
    }

    /**This method is used to print that a player has placed a bid in an auction
     * @param auction the auction currently being run
     * @param players the players in the auction
     * @param tilePosition the position on the board of the tile in auction
     */
    @Override
    public Auction.BidAdvanceToken doPlayerBid(Auction auction, Players players, int tilePosition) {
        if (this.backingInterfaces.size() > 0) {
            return this.backingInterfaces.get(0).doPlayerBid(auction, players, tilePosition);
        } else {
            throw new IllegalStateException("UNINITIALIZED COMPOUND INTERFACE, AUCTION REQUIRES INITIALIZED INTERFACE");
        }
    }
}
