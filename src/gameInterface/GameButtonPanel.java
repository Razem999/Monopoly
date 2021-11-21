package gameInterface;

import gameLogic.GameActions;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The gameInterface.GameCanvas is where the drawable elements such as tiles and players are controlled
 */
public class GameButtonPanel extends JPanel {
    private final Lock actionLock;

    /**This is the constructor of GameButtonPanel with parameters
     * @param gameActions These are the players actions they can preform on their turn
     */
    public GameButtonPanel(GameActions gameActions) {
        super();

        this.actionLock = new ReentrantLock();

        this.setLayout(new GridLayout(2, 2));

        //Creates a button that when clicked rolls the dice for the current player
        JButton rollButton = new JButton("Roll");
        rollButton.addActionListener(e -> new Thread(() -> {
            if (actionLock.tryLock()) {
                try {
                    gameActions.currentPlayerRoll();
                } finally {
                    actionLock.unlock();
                }
            }
        }).start());

        //Creates a button that when clicked passes the turn of the current player
        JButton passButton = new JButton("Pass");
        passButton.addActionListener(e -> new Thread(() -> {
            if (actionLock.tryLock()) {
                try {
                    gameActions.currentPlayerPass();
                } finally {
                    actionLock.unlock();
                }
            }
        }).start());

        //Creates a button that when clicked buys the property that the current player just landed on
        JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(e -> new Thread(() -> {
            if (actionLock.tryLock()) {
                try {
                    gameActions.currentPlayerBuy();
                } finally {
                    actionLock.unlock();
                }
            }
        }).start());

        //Creates a button that when clicked starts an auction for the tile the current player just landed on
        JButton auctionButton = new JButton("Auction");
        auctionButton.addActionListener(e -> new Thread(() -> {
            if (actionLock.tryLock()) {
                try {
                    gameActions.currentPlayerStartAuction();
                } finally {
                    actionLock.unlock();
                }
            }
        }).start());


        //adding buttons to screen
        this.add(rollButton);
        this.add(passButton);
        this.add(buyButton);
        this.add(auctionButton);

        this.setBorder(new EmptyBorder(70, 50, 70, 50));
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = this.getParent();
        return new Dimension(parent.getWidth() / 2, parent.getHeight());
    }
}
