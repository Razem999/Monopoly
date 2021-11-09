package gameInterface;

import gameLogic.GameBoard;
import gameLogic.Player;
import gameLogic.Players;
import tiles.BuyableI;
import tiles.GameTileI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameTextBox extends JPanel implements GameInterfaceI {
    private final JLabel label;

    private final List<String> history;

    public GameTextBox() {
        super();

        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        this.label = new JLabel("<html></html>");
        this.add(this.label);

        this.history = new ArrayList<>();

        updateLabelText();
    }

    private void updateLabelText() {
        StringBuilder labelTextStringBuilder = new StringBuilder();

        labelTextStringBuilder.append("<html>");

        for (String text : history) {
            labelTextStringBuilder.append(text);
            labelTextStringBuilder.append("<br>");
        }

        labelTextStringBuilder.append("</html>");
        this.label.setText(labelTextStringBuilder.toString());
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = this.getParent();
        return new Dimension(parent.getWidth() / 2, parent.getHeight());
    }

    @Override
    public void startAuction(int startingBid, BuyableI tile, Players players) {
        history.add("An Auction has started!");
    }

    @Override
    public boolean processSale(String tileName, int amount, Player buyer) {
        return false;
    }

    @Override
    public void displayPlayerProperties(Player player, GameBoard gameBoard) {

    }

    @Override
    public void notifyPlayerDeclinedPurchase(Player player, String tileName) {
        history.add("Player has declined to purchase this property");
    }

    @Override
    public void notifyPlayerPurchaseConfirm(Player player, String tileName, int amount) {
        history.add("Player " + player.getPlayerID() + " has bought " + tileName + " for $" + amount);
    }

    @Override
    public void notifyRentPayment(Player owner, Player player, int amount) {
        history.add("Player " + player.getPlayerID() + " has payed Player " + owner.getPlayerID() + " $" + amount);
    }

    @Override
    public void notifyBankruptcy(Player player) {
        history.add("Player " + player.getPlayerID() + " has gone bankrupt");
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

        updateLabelText();
    }

    @Override
    public void notifyPassGo(Player player) {
        history.add("Player " + player.getPlayerID() + " has passed Go");
    }

    @Override
    public void notifyPlayerMovement(Player player, int tilesMoved, int newPosition, String destinationDescription) {

    }

    @Override
    public void notifyCannotRoll(Player player) {
        history.add("Player " + player.getPlayerID() + " can not roll");
    }

    @Override
    public void notifyCannotBuyTileKind(Player player, GameTileI tile) {
        history.add("Player " + player.getPlayerID() + " can not buy " + tile.getName());
    }

    @Override
    public void notifyCannotBuyAlreadyOwned(Player player, Player owner, GameTileI tile) {
        history.add("Player " + player.getPlayerID() + " can not buy " + tile.getName() + " because Player " + owner.getPlayerID() + "has already purchased it");
    }

    @Override
    public void notifyCannotBuyTileBalanceReasons(Player player, GameTileI tile) {
        history.add("Player " + player.getPlayerID() + " can not buy " + tile.getName() + " because balance is insufficient");
    }

    @Override
    public void notifyPlayerOwnsThis(Player owner) {
        history.add("Player " + owner.getPlayerID() + " owns this tile");
    }

    @Override
    public void notifyPlayerSentToJail(Player player) {
        history.add("Player " + player.getPlayerID() + " has been sent to jail");
    }

    @Override
    public void notifyPlayerLeftJail(Player player) {
        history.add("Player " + player.getPlayerID() + " has left jail");
    }

    @Override
    public void notifyPlayerStayJail(Player player) {
        history.add("Player " + player.getPlayerID() + " has stayed in jail");
    }

    @Override
    public void notifyFreeParkingDeposit(Player player, int amount) {
        history.add("Player " + player.getPlayerID() + " has deposited $" + amount + " too free parking");
    }

    @Override
    public void notifyAuctionCannotStart(GameTileI tile) {
        history.add("The Auction can not start");
    }

    @Override
    public void notifyAuctionBetLow(Player player, int amount) {

    }

    @Override
    public void notifyPlayerTaxPayment(Player player, int amount) {
        history.add("Player " + player.getPlayerID() + " has payed $" + amount + " in taxes");
    }

    @Override
    public void notifyPlayerEndedTurn(Player player) {
        history.add("Player " + player.getPlayerID() + " has ended their turn");
    }

    @Override
    public void notifyPlayerTurn(Player player) {
        history.add("It is Player " + player.getPlayerID() + "'s turn to play");
    }

    @Override
    public void notifyPlayerMustRoll(Player player) {
        history.add("Player " + player.getPlayerID() + " has yet to roll");
    }
}
