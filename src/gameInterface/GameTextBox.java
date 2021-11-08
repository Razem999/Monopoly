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

    }

    @Override
    public void notifyPlayerPurchaseConfirm(Player player, String tileName, int amount) {

    }

    @Override
    public void notifyRentPayment(Player owner, Player payer, int amount) {

    }

    @Override
    public void notifyBankruptcy(Player player) {

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

    }

    @Override
    public void notifyPlayerMovement(Player player, int tilesMoved, int newPosition, String destinationDescription) {

    }

    @Override
    public void notifyCannotRoll(Player player) {

    }

    @Override
    public void notifyCannotBuyTileKind(Player player, GameTileI tile) {

    }

    @Override
    public void notifyCannotBuyAlreadyOwned(Player player, Player owner, GameTileI tile) {

    }

    @Override
    public void notifyCannotBuyTileBalanceReasons(Player player, GameTileI tile) {

    }

    @Override
    public void notifyPlayerOwnsThis(Player owner) {

    }

    @Override
    public void notifyPlayerSentToJail(Player player) {

    }

    @Override
    public void notifyPlayerLeftJail(Player player) {

    }

    @Override
    public void notifyPlayerStayJail(Player player) {

    }

    @Override
    public void notifyFreeParkingDeposit(Player player, int amount) {

    }

    @Override
    public void notifyAuctionCannotStart(GameTileI tile) {

    }

    @Override
    public void notifyPlayerTaxPayment(Player player, int amount) {

    }

    @Override
    public void notifyPlayerEndedTurn(Player player) {

    }

    @Override
    public void notifyPlayerTurn(Player player) {

    }

    @Override
    public void notifyPlayerMustRoll(Player player) {

    }
}
