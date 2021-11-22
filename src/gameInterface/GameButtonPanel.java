package gameInterface;

import gameLogic.GameActions;
import gameLogic.GameBoard;
import gameLogic.Player;
import gameLogic.Players;
import tiles.GameTile;
import tiles.JailTile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The gameInterface.GameCanvas is where the drawable elements such as tiles and players are controlled
 */
public class GameButtonPanel extends JPanel implements Player.PlayerChangeListener {
    private enum UIState {
        JailedPlayer,
        NormalPlayer,
    }

    private UIState uiState;
    private final Lock actionLock;

    private final GameBoard gameBoard;
    private final Players players;

    private final JButton rollButton;
    private final JButton passButton;
    private final JButton buyButton;
    private final JButton auctionButton;
    private final JButton payJailFeeButton;

    /**This is the constructor of GameButtonPanel with parameters
     * @param gameActions These are the players actions they can preform on their turn
     */
    public GameButtonPanel(GameActions gameActions, GameBoard gameBoard, Players players) {
        super();

        this.uiState = UIState.NormalPlayer;
        this.gameBoard = gameBoard;
        this.players = players;
        this.players.addPlayerChangeListener(this);
        this.actionLock = new ReentrantLock();

        //Creates a button that when clicked rolls the dice for the current player
        this.rollButton = new JButton("Roll");
        this.rollButton.addActionListener(e -> new Thread(() -> {
            if (actionLock.tryLock()) {
                try {
                    gameActions.currentPlayerRoll();
                } finally {
                    actionLock.unlock();
                }
            }
        }).start());

        //Creates a button that when clicked passes the turn of the current player
        this.passButton = new JButton("Pass");
        this.passButton.addActionListener(e -> new Thread(() -> {
            if (actionLock.tryLock()) {
                try {
                    gameActions.currentPlayerPass();
                } finally {
                    actionLock.unlock();
                }
            }
        }).start());

        //Creates a button that when clicked buys the property that the current player just landed on
        this.buyButton = new JButton("Buy");
        this.buyButton.addActionListener(e -> new Thread(() -> {
            if (actionLock.tryLock()) {
                try {
                    gameActions.currentPlayerBuy();
                } finally {
                    actionLock.unlock();
                }
            }
        }).start());

        //Creates a button that when clicked starts an auction for the tile the current player just landed on
        this.auctionButton = new JButton("Auction");
        this.auctionButton.addActionListener(e -> new Thread(() -> {
            if (actionLock.tryLock()) {
                try {
                    gameActions.currentPlayerStartAuction();
                } finally {
                    actionLock.unlock();
                }
            }
        }).start());

        //Creates a button that allows a player to pay themselves out of jail
        this.payJailFeeButton = new JButton("Pay Jail Fee ($" + JailTile.JAIL_FINE + ")");
        this.payJailFeeButton.addActionListener(e -> new Thread(() -> {
            if (actionLock.tryLock()) {
                try {
                    gameActions.currentPlayerPayJailFee();
                } finally {
                    actionLock.unlock();
                }
            }

        }).start());

        this.setupForNormalPlayers();
    }

    private void setupForNormalPlayers() {
        this.removeAll();

        this.setLayout(new GridLayout(2, 2));
        //adding buttons to screen
        this.add(this.rollButton);
        this.add(this.passButton);
        this.add(this.buyButton);
        this.add(this.auctionButton);

        this.setBorder(new EmptyBorder(70, 50, 70, 50));

        this.uiState = UIState.NormalPlayer;

        this.revalidate();
        this.repaint();
    }

    private void setupForJailedPlayers() {
        this.removeAll();

        this.setLayout(new GridLayout(3, 2));
        //adding buttons to screen
        this.add(this.rollButton);
        this.add(this.passButton);
        this.add(this.buyButton);
        this.add(this.auctionButton);
        this.add(this.payJailFeeButton);

        this.setBorder(new EmptyBorder(50, 50, 50, 50));

        this.uiState = UIState.JailedPlayer;

        this.revalidate();
        this.repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = this.getParent();
        return new Dimension(parent.getWidth() / 2, parent.getHeight());
    }

    @Override
    public void handlePlayerChange(Player player) {
        if (player.equals(this.players.getCurrentPlayer())) {
            if (this.gameBoard.isPlayerInJail(player) && this.uiState == UIState.NormalPlayer) {
                this.setupForJailedPlayers();
            } else if (!this.gameBoard.isPlayerInJail(player) && this.uiState == UIState.JailedPlayer) {
                this.setupForNormalPlayers();
            }
        }
    }
}
