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
            labelTextStringBuilder.append("-----------------" + newline);
        }


        this.textArea.setText(labelTextStringBuilder.toString());
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = this.getParent();
        return new Dimension(parent.getWidth() / 2, parent.getHeight());
    }

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

    @Override
    public boolean processSale(String tileName, int amount, Player player) {
        if (player.getAIStrategy().isPresent()) {
            return true;
        }
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want top buy " + tileName + " for $" + amount + "?");

        return choice == JOptionPane.YES_OPTION;
    }

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

    @Override
    public void displayPlayerProperties(Player player, GameBoard gameBoard) {

    }

    @Override
    public void notifyPlayerDeclinedPurchase(Player player, String tileName) {
        history.add("Player has declined to purchase this property");

        update();
    }

    @Override
    public void notifyPlayerPurchaseConfirm(Player player, String tileName, int amount) {
        history.add("Player " + player.getPlayerID() + " has bought " + tileName + " for $" + amount);

        update();
    }

    @Override
    public void notifyRentPayment(Player owner, Player player, int amount) {
        history.add("Player " + player.getPlayerID() + " has payed Player " + owner.getPlayerID() + " $" + amount);

        update();
    }

    @Override
    public void notifyBankruptcy(Player player) {
        history.add("Player " + player.getPlayerID() + " has gone bankrupt");

        update();
    }

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

    @Override
    public void notifyPassGo(Player player) {
        history.add("Player " + player.getPlayerID() + " has passed Go");

        update();
    }

    @Override
    public void notifyPlayerMovement(Player player, int tilesMoved, int newPosition, String destinationDescription) {
        history.add("Player " + player.getPlayerID() + " has moved " +
                tilesMoved + " tiles onto tile number " + newPosition + ":\n" + destinationDescription);

        update();
    }

    @Override
    public void notifyCannotRoll(Player player) {
        history.add("Player " + player.getPlayerID() + " can not roll");

        update();
    }

    @Override
    public void notifyCannotBuyTileKind(Player player, GameTile tile) {
        history.add("Player " + player.getPlayerID() + " can not buy " + tile.getName());

        update();
    }

    @Override
    public void notifyCannotBuyAlreadyOwned(Player player, Player owner, GameTile tile) {
        history.add("Player " + player.getPlayerID() + " can not buy " + tile.getName() + " because Player " + owner.getPlayerID() + "has already purchased it");

        update();
    }

    @Override
    public void notifyCannotBuyTileBalanceReasons(Player player, GameTile tile) {
        history.add("Player " + player.getPlayerID() + " can not buy " + tile.getName() + " because balance is insufficient");

        update();
    }

    @Override
    public void notifyCannotBuyHouseBalanceReasons(Player player, GameTile tile) {
        history.add("Player " + player.getPlayerID() + " can not buy house because balance is insufficient");

        update();
    }

    @Override
    public void notifyCannotBuyHouseOwnershipReasons(Player player, Optional<Player> owner, GameTile tile) {
        if (owner.isPresent()) {
            JOptionPane.showMessageDialog(null, "Player " + player.getPlayerID() + " can not buy house for " + tile.getName() + " because Player " + owner.get().getPlayerID() + "owns this property");
        } else {
            JOptionPane.showMessageDialog(null, "You must own this property to upgrade it");
        }
    }

    @Override
    public void notifyCannotBuyHouseTileKind(Player player, GameTile tile) {
        history.add("Player " + player.getPlayerID() + " can not buy house in " + tile.getName());

        update();
    }

    @Override
    public void notifyCannotBuyHouseSetReasons(Player player, GameTile tile) {
        JOptionPane.showMessageDialog(null, "You must own all properties under this set to upgrade");
    }

    @Override
    public void notifyHousesUnavailable(Player player) {
        history.add("There are no houses available to buy");

        update();
    }

    @Override
    public void notifyHotelsUnavailable(Player player) {
        JOptionPane.showMessageDialog(null, "There are no hotels available");
    }

    @Override
    public void notifyPlayerPurchasedHouse(Player player, String tileName, int amount) {
        history.add("Player " + player.getPlayerID() + " has bought a house for " + tileName + " for $" + amount);

        update();
    }

    @Override
    public void notifyPlayerDeclinedHouse(Player player) {
        history.add("Player " + player.getPlayerID() + " has declined to purchase a house for this property");

        update();
    }


    @Override
    public void notifyPlayerOwnsThis(Player owner) {
        history.add("Player " + owner.getPlayerID() + " owns this tile");

        update();
    }

    @Override
    public void notifyPlayerSentToJail(Player player) {
        history.add("Player " + player.getPlayerID() + " has been sent to jail");

        update();
    }

    @Override
    public void notifyPlayerLeftJail(Player player) {
        history.add("Player " + player.getPlayerID() + " has left jail");

        update();
    }

    @Override
    public void notifyPlayerStayJail(Player player) {
        history.add("Player " + player.getPlayerID() + " has stayed in jail");

        update();
    }

    @Override
    public void notifyFreeParkingDeposit(Player player, int amount) {
        history.add("Player " + player.getPlayerID() + " has deposited $" + amount + " too free parking");

        update();
    }

    @Override
    public void notifyAuctionCannotStart(GameTile tile) {
        history.add("The Auction can not start");

        update();
    }

    @Override
    public void notifyPlayerTaxPayment(Player player, int amount) {
        history.add("Player " + player.getPlayerID() + " has payed $" + amount + " in taxes");

        update();
    }

    @Override
    public void notifyPlayerEndedTurn(Player player) {
        history.add("Player " + player.getPlayerID() + " has ended their turn");

        update();
    }

    @Override
    public void notifyPlayerTurn(Player player) {
        history.add("It is Player " + player.getPlayerID() + "'s turn to play");

        update();
    }

    @Override
    public void notifyPlayerMustRoll(Player player) {
        history.add("Player " + player.getPlayerID() + " has yet to roll");

        update();
    }

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

    @Override
    public void notifyBetError(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    @Override
    public void notifyTileCannotUpgradeFurther(Player player, PropertyTile tile) {
        JOptionPane.showMessageDialog(null, "This property has reached the maximum upgrade");
    }

    @Override
    public void notifyNoTilesApplicable() {
        JOptionPane.showMessageDialog(null, "No tiles can be used for this.");
    }

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
