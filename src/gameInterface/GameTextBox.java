package gameInterface;

import gameLogic.Auction;
import gameLogic.GameBoard;
import gameLogic.Player;
import gameLogic.Players;
import tiles.BuyableI;
import tiles.GameTileI;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameTextBox extends JPanel implements GameInterfaceI {
    private final JTextArea textArea;

    private final List<String> history;
    private final static String newline = "\n";

    public GameTextBox() {
        super();

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
        StringBuilder labelTextStringBuilder = new StringBuilder();

        for (String text : history) {
            labelTextStringBuilder.append(text + newline);
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
    public void startAuction(int startingBid, BuyableI tile, Players players) {
        history.add("An auction is starting for " + tile.getName() + " for $" + startingBid);
        Auction auction = new Auction(players.getPlayersList(), players.getCurrentPlayer());

        while (!auction.shouldEnd()) {
            history.add("The current highest bid is $" + auction.getPrice());
            doPlayerBid(auction);
        }

        if (auction.getHighestBidder() != null) {
            tile.closeAuctionFor(auction.getPrice(), auction.getHighestBidder());
        } else {
            history.add("No one has purchased this property");
        }

        update();
    }

    /**This method is used to take the amount the Player bid and display it to other Players. If the Player
     * enters quit as their bid, the Player withdraws from the auction.
     * @param auction This is the auction the Players are partaking in
     */
    private void doPlayerBid(Auction auction) {
        Player currentBidder = auction.getCurrentBidder();
        int currentBidderBalance = auction.getCurrentBidderBalance();
        history.add("Player " + currentBidder.getPlayerID() + " is placing a bet for (Press Cancel to withdraw): ");

        while (true) {
            update();
            String betInput = JOptionPane.showInputDialog("Enter bet amount (Current Bid - $" + auction.getPrice() + ") : ");
            if (betInput == null) {
                auction.withdrawCurrentPlayerFromAuction();
                break;
            }
            try {
                if ((Integer.parseInt(betInput) <= currentBidderBalance) && (Integer.parseInt(betInput) > auction.getPrice())) {
                    auction.bet(Integer.parseInt(betInput));
                    break;
                } else if (Integer.parseInt(betInput) <= auction.getPrice()) {
                    history.add(">> The that bet amount is too low! (Must be more than " + auction.getPrice() + ")");
                } else {
                    history.add(">> That bet exceeds your balance!");
                }
            } catch (NumberFormatException ex) {
                history.add(">> That input is not a number!");
            }

        }
        update();
    }

    @Override
    public boolean processSale(String tileName, int amount, Player buyer) {
        return true;
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
    public void notifyCannotBuyTileKind(Player player, GameTileI tile) {
        history.add("Player " + player.getPlayerID() + " can not buy " + tile.getName());

        update();
    }

    @Override
    public void notifyCannotBuyAlreadyOwned(Player player, Player owner, GameTileI tile) {
        history.add("Player " + player.getPlayerID() + " can not buy " + tile.getName() + " because Player " + owner.getPlayerID() + "has already purchased it");

        update();
    }

    @Override
    public void notifyCannotBuyTileBalanceReasons(Player player, GameTileI tile) {
        history.add("Player " + player.getPlayerID() + " can not buy " + tile.getName() + " because balance is insufficient");

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
    public void notifyAuctionCannotStart(GameTileI tile) {
        history.add("The Auction can not start");

        update();
    }

    @Override
    public void notifyAuctionBetLow(Player player, int amount) {

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
                    JOptionPane.showMessageDialog(this, "There must be at least one human player.");
                    break;
                }

                return new PlayerSelection(numPlayers, numAIPlayers);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "That is not a valid number.");
            }
        }

        return new PlayerSelection(4, 0);
    }
}
