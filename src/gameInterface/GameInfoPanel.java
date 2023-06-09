package gameInterface;

import gameLogic.GameActions;
import gameLogic.GameBoard;
import gameLogic.Players;
import save.SaveCreator;

import javax.swing.*;
import java.util.concurrent.locks.ReentrantLock;

public class GameInfoPanel extends JComponent {

    private final GameTextBox gameTextBox;

    public GameInfoPanel(GameActions gameActions, AuctionBidExecutor.Factory auctionBetExecutorFactory, Players players, GameBoard gameBoard, SaveCreator saveCreator) {
        super();

        ReentrantLock actionLock = new ReentrantLock();

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.gameTextBox = new GameTextBox(auctionBetExecutorFactory, actionLock);
        this.add(this.gameTextBox);

        this.add(new GameButtonPanel(gameActions, gameBoard, players, saveCreator, actionLock));
    }

    /**This method is used to get the gameInterface
     */
    public GameInterface getGameInterface() {
        return this.gameTextBox;
    }

}
