package gameInterface;

import gameLogic.*;
import tiles.BuyableTile;
import tiles.BuyableTile;
import tiles.GameTile;
import tiles.PropertyTile;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class GameTextBox extends JPanel implements GameInterface {
    private final JTextArea textArea;
    private final List<String> history;
    private final ReentrantLock actionLock;
    private final AuctionBidExecutor.Factory auctionBetExecutorFactory;


    public GameTextBox(Factory auctionBetExecutorFactory, ReentrantLock actionLock) {
        super();

        this.actionLock = actionLock;
        this.auctionBetExecutorFactory = auctionBetExecutorFactory;
        this.auctionBetExecutorFactory.setGameInterface(this);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        this.textArea = new JTextArea(15, 60);

        // Makes sure the text box scrolls down when new text is added
        DefaultCaret caret = (DefaultCaret) this.textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.add(this.textArea);

        JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scroll);

        this.history = new ArrayList<>();

        update();
    }

    private void update() {
        String newline = "\n";
        StringBuilder labelTextStringBuilder = new StringBuilder();

        for (String text : history) {
            labelTextStringBuilder.append(text).append(newline);
            labelTextStringBuilder.append("-----------------").append(newline);
        }


        this.textArea.setText(labelTextStringBuilder.toString());
    }

    /**This method is get the size the window needs to be
     */
    @Override
    public Dimension getPreferredSize() {
        Container parent = this.getParent();
        return new Dimension(parent.getWidth() / 2, parent.getHeight());
    }

    /**This method is used to print text about the auction to the text screen
     * @param startingBid the starting bid for the auction
     * @param tile the tile being auctioned
     * @param players the players in the auction
     * @param tilePosition the position of the tile that is being auctioned on the board
     */
    @Override
    public void startAuction(int startingBid, BuyableTile tile, Players players, int tilePosition) {
        JOptionPane.showMessageDialog(null, "An auction is starting for " + tile.getName() + " for $" + startingBid);
        Auction auction = new Auction(players.getPlayersList(), players.getCurrentPlayer());

        while (!auction.shouldEnd()) {
            Optional<AuctionBidExecutor> auctionBetExecutor = this.auctionBetExecutorFactory.getAuctionBetExecutor(auction.getCurrentBidder());
            auctionBetExecutor.ifPresent(ex -> ex.doPlayerBid(auction, players, tilePosition));
        }

        if (auction.getHighestBidder() != null) {
            tile.closeAuctionFor(auction.getPrice(), auction.getHighestBidder());
        } else {
            JOptionPane.showMessageDialog(null, "The auction closed without a buyer.");
        }

        update();
    }

    /**This method is used to print text about any tile sales to the text screen
     * @param tileName the name of the tile being bought
     * @param amount the amount of money the sale took
     * @param player the players making the purchase
     */
    @Override
    public boolean processSale(String tileName, int amount, Player player) {
        if (player.getAIStrategy().isPresent()) {
            return true;
        }
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want top buy " + tileName + " for $" + amount + "?");

        return choice == JOptionPane.YES_OPTION;
    }

    /**This method is used to show the tile selection for building a house
     * @param tiles the tiles available for building a house
     * @param gameBoard gives information about all the tiles
     * @param onSelection when the player makes his selection from the list
     */
    @Override
    public void getTileSelection(List<GameBoard.TileAndIndex> tiles, GameBoard gameBoard, Consumer<Optional<Integer>> onSelection) {
        SwingUtilities.invokeLater(() -> {
            if (this.actionLock.tryLock()) {
                new TileSelectionMenu(tiles, (Integer s) -> {
                    onSelection.accept(Optional.of(s));
                    this.actionLock.unlock();
                }, () -> {
                    onSelection.accept(Optional.empty());
                    this.actionLock.unlock();
                });
            }
        });
    }

    /**This method is used to print text on the text screen about the players owned properties to the text screen
     * @param player the player whose information is to be displayed
     * @param gameBoard information on all the tiles
     */
    @Override
    public void displayPlayerProperties(Player player, GameBoard gameBoard) {

    }

    /**This method is used to print text on the text screen about the player declining a purchase of a tile
     * @param player the player that declined to purchase
     * @param tileName the name of the tile not being declined for purchased
     */
    @Override
    public void notifyPlayerDeclinedPurchase(Player player, String tileName) {
        history.add("Player has declined to purchase this property");

        update();
    }

    /**This method is used to print text on the text screen about the player purchasing a tile
     * @param player the player that purchased the property
     * @param tileName the name of the tile being purchased
     * @param amount the cost of the purchase
     */
    @Override
    public void notifyPlayerPurchaseConfirm(Player player, String tileName, int amount) {
        history.add("Player " + player.getPlayerID() + " has bought " + tileName + " for $" + amount);

        update();
    }

    /**This method is used to print text on the text screen about the player paying rent
     * @param owner the owner of the property receiving rent
     * @param player the player paying rent
     * @param amount the cost of the rent
     */
    @Override
    public void notifyRentPayment(Player owner, Player player, int amount) {
        history.add("Player " + player.getPlayerID() + " has payed Player " + owner.getPlayerID() + " $" + amount);

        update();
    }

    /**This method is used to print text on the text screen about a player going bankrupt
     * @param player the player that has gone bankrupt
     */
    @Override
    public void notifyBankruptcy(Player player) {
        history.add("Player " + player.getPlayerID() + " has gone bankrupt");

        update();
    }

    /**This method is used to print text on the text screen about a player who rolled dice
     * @param player the player that has rolled the dice
     * @param firstRoll the value of the first dice rolled
     * @param secondRoll the value of the second dice rolled
     */
    @Override
    public void notifyRoll(Player player, int firstRoll, int secondRoll) {
        int rollTotal = firstRoll + secondRoll;
        if (firstRoll == secondRoll) {
            history.add("Player " + player.getPlayerID() + " got a double roll of " +
                    rollTotal + "! (" + firstRoll + ", " + secondRoll + ")");
        } else {
            history.add("Player " + player.getPlayerID() + " rolled " +
                    rollTotal + " (" + firstRoll + ", " + secondRoll + ")");
        }

        update();
    }

    /**This method is used to print text on the text screen about a player who passed go
     * @param player the player that has passed go
     */
    @Override
    public void notifyPassGo(Player player) {
        history.add("Player " + player.getPlayerID() + " has passed Go");

        update();
    }

    /**This method is used to print text on the text screen about a player who has moved around the board
     * @param player the player that has moved
     * @param tilesMoved the number of tiles moved
     * @param newPosition the position on the board the player has moved to
     * @param destinationDescription the description of the tile the player has moved to
     */
    @Override
    public void notifyPlayerMovement(Player player, int tilesMoved, int newPosition, String destinationDescription) {
        history.add("Player " + player.getPlayerID() + " has moved " +
                tilesMoved + " tiles onto tile number " + newPosition + ":\n" + destinationDescription);

        update();
    }

    /**This method is used to print text on the text screen about a player who cannot roll the dice
     * @param player the player that cannot roll the dice
     */
    @Override
    public void notifyCannotRoll(Player player) {
        history.add("Player " + player.getPlayerID() + " can not roll");

        update();
    }

    /**This method is used to print text on the text screen about a player who cannot buy a property
     * due to its tile type
     * @param player the player trying to buy the tile
     * @param tile the tile that cannot be bought
     */
    @Override
    public void notifyCannotBuyTileKind(Player player, GameTile tile) {
        history.add("Player " + player.getPlayerID() + " can not buy " + tile.getName());

        update();
    }

    /**This method is used to print text on the text screen about a player who cannot buy a property
     * due to it being owned
     * @param player the player trying to buy the tile
     * @param owner the owner of the tile
     * @param tile the tile that cannot be bought
     */
    @Override
    public void notifyCannotBuyAlreadyOwned(Player player, Player owner, GameTile tile) {
        history.add("Player " + player.getPlayerID() + " can not buy " + tile.getName() + " because Player " + owner.getPlayerID() + "has already purchased it");

        update();
    }

    /**This method is used to print text on the text screen about a player who cannot buy a property
     * due to insufficient funds
     * @param player the player trying to buy the tile
     * @param tile the tile that cannot be bought
     */
    @Override
    public void notifyCannotBuyTileBalanceReasons(Player player, GameTile tile) {
        history.add("Player " + player.getPlayerID() + " can not buy " + tile.getName() + " because balance is insufficient");

        update();
    }

    /**This method is used to print text on the text screen about a player who cannot buy a house
     * due to insufficient funds
     * @param player the player trying to buy the house
     * @param tile the tile that cannot have a house be bought on
     */
    @Override
    public void notifyCannotBuyHouseBalanceReasons(Player player, GameTile tile) {
        history.add("Player " + player.getPlayerID() + " can not buy house because balance is insufficient");

        update();
    }

    /**This method is used to print text on the text screen about a player who cannot buy a house
     * due to not owning the tile
     * @param player the player trying to buy the house
     * @param owner the owner of the tile if there is one
     * @param tile the tile that cannot have a house be bought on
     */
    @Override
    public void notifyCannotBuyHouseOwnershipReasons(Player player, Optional<Player> owner, GameTile tile) {
        if (owner.isPresent()) {
            JOptionPane.showMessageDialog(null, "Player " + player.getPlayerID() + " can not buy house for " + tile.getName() + " because Player " + owner.get().getPlayerID() + "owns this property");
        } else {
            JOptionPane.showMessageDialog(null, "You must own this property to upgrade it");
        }
    }

    /**This method is used to print text on the text screen about a player who cannot buy a house
     * due to the tile not being the right type
     * @param player the player trying to buy the house
     * @param tile the tile that cannot have a house be bought on
     */
    @Override
    public void notifyCannotBuyHouseTileKind(Player player, GameTile tile) {
        history.add("Player " + player.getPlayerID() + " can not buy house in " + tile.getName());

        update();
    }

    /**This method is used to print text on the text screen about a player who cannot buy a house
     * due to the player not owning all properties in the tile set
     * @param player the player trying to buy the house
     * @param tile the tile that cannot have a house be bought on
     */
    @Override
    public void notifyCannotBuyHouseSetReasons(Player player, GameTile tile) {
        JOptionPane.showMessageDialog(null, "You must own all properties under this set to upgrade");
    }

    /**This method is used to print text on the text screen about a player who cannot buy a house
     * due to the max number of houses being reached
     * @param player the player trying to buy the house
     */
    @Override
    public void notifyHousesUnavailable(Player player) {
        history.add("There are no houses available to buy");

        update();
    }

    /**This method is used to print text on the text screen about a player who cannot buy a hotel
     * due to the max number of hotels being reached
     * @param player the player trying to buy the hotel
     */
    @Override
    public void notifyHotelsUnavailable(Player player) {
        JOptionPane.showMessageDialog(null, "There are no hotels available");
    }

    /**This method is used to print text on the text screen about a player purchasing a house
     * @param player the player who bought the house
     * @param tileName the name of the tile that the house is being placed on
     * @param amount the amount the house costs
     */
    @Override
    public void notifyPlayerPurchasedHouse(Player player, String tileName, int amount) {
        history.add("Player " + player.getPlayerID() + " has bought a house for " + tileName + " for $" + amount);

        update();
    }

    /**This method is used to print text on the text screen about a player declining the purchasing a house
     * @param player the player who decline to purchase the house
     */
    @Override
    public void notifyPlayerDeclinedHouse(Player player) {
        history.add("Player " + player.getPlayerID() + " has declined to purchase a house for this property");

        update();
    }


    /**This method is used to print text on the text screen about a player owning a tile
     * @param owner the player who owns the tile
     */
    @Override
    public void notifyPlayerOwnsThis(Player owner) {
        history.add("Player " + owner.getPlayerID() + " owns this tile");

        update();
    }

    /**This method is used to print text on the text screen about a player going to jail
     * @param player the player being sent to jail
     */
    @Override
    public void notifyPlayerSentToJail(Player player) {
        history.add("Player " + player.getPlayerID() + " has been sent to jail");

        update();
    }

    /**This method is used to print text on the text screen about a player leaving to jail
     * @param player the player being leaving jail
     */
    @Override
    public void notifyPlayerLeftJail(Player player) {
        history.add("Player " + player.getPlayerID() + " has left jail");

        update();
    }

    /**This method is used to print text on the text screen about a player staying in jail
     * @param player the player staying in jail
     */
    @Override
    public void notifyPlayerStayJail(Player player) {
        history.add("Player " + player.getPlayerID() + " has stayed in jail");

        update();
    }

    /**This method is used to print text on the text screen about a player adding funds to freeparking
     * @param player the player being sent to jail
     * @param amount the amount being added to freeparking
     */
    @Override
    public void notifyFreeParkingDeposit(Player player, int amount) {
        history.add("Player " + player.getPlayerID() + " has deposited $" + amount + " too free parking");

        update();
    }

    /**This method is used to print text on the text screen about an auction not being able to start
     * @param tile the tile that an auction cannot be held for
     */
    @Override
    public void notifyAuctionCannotStart(GameTile tile) {
        history.add("The Auction can not start");

        update();
    }

    /**This method is used to print text on the text screen about a player paying taxes
     * @param player the player paying taxes
     * @param amount the amount of money paid in taxes
     */
    @Override
    public void notifyPlayerTaxPayment(Player player, int amount) {
        history.add("Player " + player.getPlayerID() + " has payed $" + amount + " in taxes");

        update();
    }

    /**This method is used to print text on the text screen about a player ending their turn
     * @param player the player ending their turn
     */
    @Override
    public void notifyPlayerEndedTurn(Player player) {
        history.add("Player " + player.getPlayerID() + " has ended their turn");

        update();
    }

    /**This method is used to print text on the text screen about a player whose turn it is
     * @param player the player whose turn it is
     */
    @Override
    public void notifyPlayerTurn(Player player) {
        history.add("It is Player " + player.getPlayerID() + "'s turn to play");

        update();
    }

    /**This method is used to print text on the text screen about a player still needing to roll
     * @param player the player still needing to roll
     */
    @Override
    public void notifyPlayerMustRoll(Player player) {
        history.add("Player " + player.getPlayerID() + " has yet to roll");

        update();
    }

    /**This method is used to print text on the text screen about how many players are participating in the game
     */
    @Override
    public PlayerSelection askHowManyPlayers() {
        while (true) {
            try {
                String numPlayersInput = JOptionPane.showInputDialog("Enter the amount of players: ");
                int numPlayers = Integer.parseInt(numPlayersInput);
                String numAIPlayersInput = JOptionPane.showInputDialog("Enter the amount of AIs: ");
                int numAIPlayers = Integer.parseInt(numAIPlayersInput);

                if (numAIPlayers >= numPlayers) {
                    JOptionPane.showMessageDialog(null, "There must be at least one human player.");
                    break;
                }

                return new PlayerSelection(numPlayers, numAIPlayers);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "That is not a valid number.");
            }
        }

        return new PlayerSelection(4, 0);
    }

    /**This method is used to print text on the text screen about an error when betting in an auction
     * @param msg the error message
     */
    @Override
    public void notifyBetError(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    /**This method is used to print text on the text screen about a tile that cannot have anymore houses
     * @param player the player trying to buy the house/hotel
     * @param tile the tile that cannot be upgraded anymore
     */
    @Override
    public void notifyTileCannotUpgradeFurther(Player player, PropertyTile tile) {
        JOptionPane.showMessageDialog(null, "This property has reached the maximum upgrade");
    }

    /**This method is used to print text on the text screen about no tiles being owned that can have houses
     */
    @Override
    public void notifyNoTilesApplicable() {
        JOptionPane.showMessageDialog(null, "No tiles can be used for this.");
    }

    /**This method is used to print text on the text screen about a player biding in an auction
     * @param auction the auction being held
     * @param players the players in the auction
     * @param tilePosition the position of the tile being auctioned on the board
     */
    @Override
    public Auction.BidAdvanceToken doPlayerBid(Auction auction, Players players, int tilePosition) {
        while (true) {
            update();
            String betInput = JOptionPane.showInputDialog("Enter bet amount (Current Bid - $" + auction.getPrice() + ") : ");
            if (betInput == null) {
                return auction.withdrawCurrentPlayerFromAuction();
            }
            try {
                auction.showBetErrorIfBetInvalid(Integer.parseInt(betInput), this);
                Optional<Auction.BidAdvanceToken> token = auction.bid(Integer.parseInt(betInput));

                if (token.isPresent()) {
                    return token.get();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "That bet is not a number!");
            }
        }

    }
}
