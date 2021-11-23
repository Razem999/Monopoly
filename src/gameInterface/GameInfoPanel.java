package gameInterface;

import gameLogic.GameActions;
import gameLogic.GameBoard;
import gameLogic.Players;

import javax.swing.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameInfoPanel extends JComponent {

    private final GameTextBox gameTextBox;

    public GameInfoPanel(GameActions gameActions, AuctionBidExecutor.Factory auctionBetExecutorFactory, Players players, GameBoard gameBoard) {
        super();

        ReentrantLock actionLock = new ReentrantLock();

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.gameTextBox = new GameTextBox(auctionBetExecutorFactory, actionLock);
        this.add(this.gameTextBox);

        this.add(new GameButtonPanel(gameActions, gameBoard, players, actionLock));
    }

    public GameInterface getGameInterface() {
        return this.gameTextBox;
    }

}
